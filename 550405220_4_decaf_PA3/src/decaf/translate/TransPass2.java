package decaf.translate;

import java.util.Stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import decaf.tree.Tree;
import decaf.backend.OffsetCounter;
import decaf.machdesc.Intrinsic;
import decaf.symbol.Variable;
import decaf.symbol.Class;
import decaf.tac.Label;
import decaf.tac.Temp;
import decaf.tac.VTable;
import decaf.type.BaseType;

public class TransPass2 extends Tree.Visitor {

	private Translater tr;

	private Temp currentThis;

	private Stack<Label> loopExits;
	
	private Stack<Label> loopEnds;
	
	private Map<String, Integer> map = new HashMap<String, Integer>();

	public TransPass2(Translater tr) {
		this.tr = tr;
		loopExits = new Stack<Label>();
		loopEnds = new Stack<Label>();
	}

	@Override
	public void visitClassDef(Tree.ClassDef classDef) {
		for (Tree f : classDef.fields) {
			f.accept(this);
		}
	}

	@Override
	public void visitMethodDef(Tree.MethodDef funcDefn) {
		if (!funcDefn.statik) {
			currentThis = ((Variable) funcDefn.symbol.getAssociatedScope()
					.lookup("this")).getTemp();
		}
		tr.beginFunc(funcDefn.symbol);
		funcDefn.body.accept(this);
		tr.endFunc();
		currentThis = null;
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		for (Tree.ClassDef cd : program.classes) {
			cd.accept(this);
			map.put(cd.symbol.getName(), cd.symbol.getSize());
		}
	}

	@Override
	public void visitVarDef(Tree.VarDef varDef) {
		if (varDef.symbol.isLocalVar()) {
			Temp t = Temp.createTempI4();
			t.sym = varDef.symbol;
			varDef.symbol.setTemp(t);
		}
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.left.accept(this);
		expr.right.accept(this);
		switch (expr.tag) {
		case Tree.PLUS:
			expr.val = tr.genAdd(expr.left.val, expr.right.val);
			break;
		case Tree.MINUS:
			expr.val = tr.genSub(expr.left.val, expr.right.val);
			break;
		case Tree.MUL:
			expr.val = tr.genMul(expr.left.val, expr.right.val);
			break;
		case Tree.DIV:
			expr.val = tr.genDiv(expr.left.val, expr.right.val);
			break;
		case Tree.MOD:
			expr.val = tr.genMod(expr.left.val, expr.right.val);
			break;
		case Tree.AND:
			expr.val = tr.genLAnd(expr.left.val, expr.right.val);
			break;
		case Tree.OR:
			expr.val = tr.genLOr(expr.left.val, expr.right.val);
			break;
		case Tree.LT:
			expr.val = tr.genLes(expr.left.val, expr.right.val);
			break;
		case Tree.LE:
			expr.val = tr.genLeq(expr.left.val, expr.right.val);
			break;
		case Tree.GT:
			expr.val = tr.genGtr(expr.left.val, expr.right.val);
			break;
		case Tree.GE:
			expr.val = tr.genGeq(expr.left.val, expr.right.val);
			break;
		case Tree.EQ:
		case Tree.NE:
			genEquNeq(expr);
			break;
		case Tree.PCLONE:
			genPclone(expr);
			break;
		}
	}
	
	@Override
	public void visitTernary(Tree.Ternary expr) {
		expr.left.accept(this);
		expr.val = tr.genLoadImm4(0);
		Label falseLabel = Label.createLabel();
		tr.genBeqz(expr.left.val, falseLabel);
		expr.middle.accept(this);
		tr.genAssign(expr.val, expr.middle.val);
		Label exit = Label.createLabel();
		tr.genBranch(exit);
		tr.genMark(falseLabel);
		expr.right.accept(this);
		tr.genAssign(expr.val, expr.right.val);
		tr.genMark(exit);
	}

	private void genEquNeq(Tree.Binary expr) {
		if (expr.left.type.equal(BaseType.STRING)
				|| expr.right.type.equal(BaseType.STRING)) {
			tr.genParm(expr.left.val);
			tr.genParm(expr.right.val);
			expr.val = tr.genDirectCall(Intrinsic.STRING_EQUAL.label,
					BaseType.BOOL);
			if(expr.tag == Tree.NE){
				expr.val = tr.genLNot(expr.val);
			}
		} else {
			if(expr.tag == Tree.EQ)
				expr.val = tr.genEqu(expr.left.val, expr.right.val);
			else
				expr.val = tr.genNeq(expr.left.val, expr.right.val);
		}
	}
	
	private void genPclone(Tree.Binary expr) {
		//find min common parent
		Label leftExit = Label.createLabel();
		Label leftLoop = Label.createLabel();
		Label rightLoop = Label.createLabel();
		Label rightExit = Label.createLabel();
		Temp leftVtable = tr.genLoad(expr.left.val, 0);
		tr.genMark(leftLoop);
		tr.genBeqz(leftVtable, leftExit);
		Temp rightVtable = tr.genLoad(expr.right.val, 0);
		tr.genMark(rightLoop);
		tr.genBeqz(rightVtable, rightExit);
		Temp cond = tr.genEqu(leftVtable, rightVtable);
		tr.genBnez(cond, leftExit);
		tr.genAssign(rightVtable, tr.genLoad(rightVtable, 0));
		tr.genBranch(rightLoop);
		tr.genMark(rightExit);
		tr.genAssign(leftVtable, tr.genLoad(leftVtable, 0));
		tr.genBranch(leftLoop);
		tr.genMark(leftExit);
		
		//create new class for dst
		Temp commonParent = Temp.createTempI4();
		Temp size = tr.genLoad(leftVtable, 4);
		Temp mul = Temp.createTempI4();
		tr.genAssign(mul, Temp.createConstTemp(4));
		tr.genAssign(size, tr.genMul(size, mul));
		tr.genParm(size);
		tr.genAssign(commonParent, tr.genDirectCall(Intrinsic.ALLOCATE.label, expr.left.type));
		
		//copy a's variable to dst
		Label copyLabel = Label.createLabel();
		Temp srcpos = Temp.createTempI4();
		Temp dstpos = Temp.createTempI4();
		Temp offset = Temp.createTempI4();
		tr.genAssign(offset, Temp.createConstTemp(4));
		tr.genAssign(srcpos, expr.left.val);
		tr.genAssign(dstpos, commonParent);
		tr.genMark(copyLabel);
		tr.genStore(tr.genLoad(srcpos, 0), dstpos, 0);
		tr.genAssign(srcpos, tr.genAdd(srcpos, offset));
		tr.genAssign(dstpos, tr.genAdd(dstpos, offset));
		tr.genAssign(size, tr.genSub(size, offset));
		tr.genBnez(size, copyLabel);
		
		//change dst's vtable to common parent vtable
		tr.genStore(leftVtable, commonParent, 0);
		expr.val = commonParent;

		
	}
	
	@Override
	public void visitAssign(Tree.Assign assign) {
		assign.left.accept(this);
		assign.expr.accept(this);
		switch (assign.left.lvKind) {
		case ARRAY_ELEMENT:
			Tree.Indexed arrayRef = (Tree.Indexed) assign.left;
			Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
			Temp t = tr.genMul(arrayRef.index.val, esz);
			Temp base = tr.genAdd(arrayRef.array.val, t);
			tr.genStore(assign.expr.val, base, 0);
			break;
		case MEMBER_VAR:
			Tree.Ident varRef = (Tree.Ident) assign.left;
			tr.genStore(assign.expr.val, varRef.owner.val, varRef.symbol
					.getOffset());
			break;
		case PARAM_VAR:
		case LOCAL_VAR:
			tr.genAssign(((Tree.Ident) assign.left).symbol.getTemp(),
					assign.expr.val);
			break;
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.val = tr.genLoadImm4(((Integer)literal.value).intValue());
			break;
		case Tree.BOOL:
			literal.val = tr.genLoadImm4((Boolean)(literal.value) ? 1 : 0);
			break;
		default:
			literal.val = tr.genLoadStrConst((String)literal.value);
		}
	}

	@Override
	public void visitExec(Tree.Exec exec) {
		exec.expr.accept(this);
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		switch (expr.tag){
		case Tree.NEG:
			expr.val = tr.genNeg(expr.expr.val);
			break;
		default:
			expr.val = tr.genLNot(expr.expr.val);
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.val = tr.genLoadImm4(0);
	}

	@Override
	public void visitBlock(Tree.Block block) {
		for (Tree s : block.block) {
			s.accept(this);
		}
	}
	
	@Override
	public void visitThisExpr(Tree.ThisExpr thisExpr) {
		thisExpr.val = currentThis;
	}

	@Override
	public void visitReadIntExpr(Tree.ReadIntExpr readIntExpr) {
		readIntExpr.val = tr.genIntrinsicCall(Intrinsic.READ_INT);
	}

	@Override
	public void visitReadLineExpr(Tree.ReadLineExpr readStringExpr) {
		readStringExpr.val = tr.genIntrinsicCall(Intrinsic.READ_LINE);
	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
			tr.genReturn(returnStmt.expr.val);
		} else {
			tr.genReturn(null);
		}

	}

	@Override
	public void visitPrint(Tree.Print printStmt) {
		for (Tree.Expr r : printStmt.exprs) {
			r.accept(this);
			tr.genParm(r.val);
			if (r.type.equal(BaseType.BOOL)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_BOOL);
			} else if (r.type.equal(BaseType.INT)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			} else if (r.type.equal(BaseType.STRING)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
			}
		}
	}

	@Override
	public void visitIndexed(Tree.Indexed indexed) {
		indexed.array.accept(this);
		indexed.index.accept(this);
		tr.genCheckArrayIndex(indexed.array.val, indexed.index.val);
		
		Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
		Temp t = tr.genMul(indexed.index.val, esz);
		Temp base = tr.genAdd(indexed.array.val, t);
		indexed.val = tr.genLoad(base, 0);
	}

	@Override
	public void visitIdent(Tree.Ident ident) {
		if(ident.lvKind == Tree.LValue.Kind.MEMBER_VAR){
			ident.owner.accept(this);
		}
		
		switch (ident.lvKind) {
		case MEMBER_VAR:
			ident.val = tr.genLoad(ident.owner.val, ident.symbol.getOffset());
			break;
		default:
			ident.val = ident.symbol.getTemp();
			break;
		}
	}
	
	@Override
	public void visitBreak(Tree.Break breakStmt) {
		tr.genBranch(loopExits.peek());
	}
	
	@Override
	public void visitContinue(Tree.Continue continueStmt) {
		tr.genBranch(loopEnds.peek());
	}
	
	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {
		if (callExpr.isArrayLength) {
			callExpr.receiver.accept(this);
			callExpr.val = tr.genLoad(callExpr.receiver.val,
					-OffsetCounter.WORD_SIZE);
		} else {
			if (callExpr.receiver != null) {
				callExpr.receiver.accept(this);
			}
			for (Tree.Expr expr : callExpr.actuals) {
				expr.accept(this);
			}
			if (callExpr.receiver != null) {
				tr.genParm(callExpr.receiver.val);
			}
			for (Tree.Expr expr : callExpr.actuals) {
				tr.genParm(expr.val);
			}
			if (callExpr.receiver == null) {
				callExpr.val = tr.genDirectCall(
						callExpr.symbol.getFuncty().label, callExpr.symbol
								.getReturnType());
			} else {
				Temp vt = tr.genLoad(callExpr.receiver.val, 0);
				Temp func = tr.genLoad(vt, callExpr.symbol.getOffset());
				callExpr.val = tr.genIndirectCall(func, callExpr.symbol
						.getReturnType());
			}
		}

	}

	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		Label cond = Label.createLabel();
		Label loop = Label.createLabel();
		tr.genBranch(cond);
		tr.genMark(loop);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		tr.genMark(cond);
		forLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(forLoop.condition.val, exit);
		loopExits.push(exit);
		loopEnds.push(loop);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		loopEnds.pop();
		tr.genMark(exit);
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		ifStmt.condition.accept(this);
		if (ifStmt.falseBranch != null) {
			Label falseLabel = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, falseLabel);
			ifStmt.trueBranch.accept(this);
			Label exit = Label.createLabel();
			tr.genBranch(exit);
			tr.genMark(falseLabel);
			ifStmt.falseBranch.accept(this);
			tr.genMark(exit);
		} else if (ifStmt.trueBranch != null) {
			Label exit = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, exit);
			if (ifStmt.trueBranch != null) {
				ifStmt.trueBranch.accept(this);
			}
			tr.genMark(exit);
		}
	}
	
	@Override
	public void visitSwitch(Tree.Switch switchStmt) {
		switchStmt.condition.accept(this);
		Label exit = Label.createLabel();
		loopExits.push(exit);
		
		//initial label
		List<Label> caseLabelList = new ArrayList<Label>();
		for (int i=0; i<switchStmt.caseList.size(); i++) {
			Label caseLabel = Label.createLabel();
			caseLabelList.add(caseLabel);
		}
		Label defaultLabel = Label.createLabel();
		
		//set branch
		for(int i=0; i<switchStmt.caseList.size(); i++) {
			Tree.Case caseStmt = (Tree.Case)switchStmt.caseList.get(i);
			caseStmt.condition.accept(this);
			Temp cond = tr.genEqu(switchStmt.condition.val, caseStmt.condition.val);
			tr.genBnez(cond, caseLabelList.get(i));
		}
		if(switchStmt.defaultStmt != null) {
			tr.genBranch(defaultLabel);
		}
		tr.genBranch(exit);
		
		//set statement of branch
		for(int i=0; i<switchStmt.caseList.size(); i++) {
			tr.genMark(caseLabelList.get(i));
			switchStmt.caseList.get(i).accept(this);
		}
		if(switchStmt.defaultStmt != null) {
			tr.genMark(defaultLabel);
			switchStmt.defaultStmt.accept(this);
		}
		tr.genMark(exit);
		loopExits.pop();
	}
	
	@Override
	public void visitCase(Tree.Case caseStmt) {
		for (Tree s : caseStmt.slist) {
			if(s != null) {
				s.accept(this);
			}
		}
	}
	
	@Override
	public void visitDefault(Tree.Default defaultStmt) {
		for(Tree s : defaultStmt.slist) {
			if(s != null) {
				s.accept(this);
			}
		}
	}
	
	@Override
	public void visitNewArray(Tree.NewArray newArray) {
		newArray.length.accept(this);
		newArray.val = tr.genNewArray(newArray.length.val);
	}

	@Override
	public void visitNewClass(Tree.NewClass newClass) {
		newClass.val = tr.genDirectCall(newClass.symbol.getNewFuncLabel(),
				BaseType.INT);
	}

	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		Label loop = Label.createLabel();
		tr.genMark(loop);
		whileLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(whileLoop.condition.val, exit);
		loopExits.push(exit);
		loopEnds.push(loop);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		loopEnds.pop();
		tr.genMark(exit);
	}
	
	@Override
	public void visitRepeat(Tree.Repeat repeatLoop) {
		Label loop = Label.createLabel();
		Label loopEnd = Label.createLabel();
		tr.genMark(loop);
		Label exit = Label.createLabel();
		loopExits.push(exit);
		loopEnds.push(loopEnd);
		if(repeatLoop.repeatStmt != null) {
			repeatLoop.repeatStmt.accept(this);
		}
		tr.genMark(loopEnd);
		repeatLoop.condition.accept(this);
		tr.genBnez(repeatLoop.condition.val, exit);
		tr.genBranch(loop);
		tr.genMark(exit);
		loopExits.pop();
		loopEnds.pop();
	}
	
	@Override
	public void visitTypeTest(Tree.TypeTest typeTest) {
		typeTest.instance.accept(this);
		typeTest.val = tr.genInstanceof(typeTest.instance.val,
				typeTest.symbol);
	}

	@Override
	public void visitTypeCast(Tree.TypeCast typeCast) {
		typeCast.expr.accept(this);
		if (!typeCast.expr.type.compatible(typeCast.symbol.getType())) {
			tr.genClassCast(typeCast.expr.val, typeCast.symbol);
		}
		typeCast.val = typeCast.expr.val;
	}
}
