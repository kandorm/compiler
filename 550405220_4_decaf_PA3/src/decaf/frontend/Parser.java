//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//###           14 Sep 06  -- Keltin Leung-- ReduceListener support, eliminate underflow report in error recovery
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 11 "Parser.y"
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
//#line 25 "Parser.java"
interface ReduceListener {
  public boolean onReduce(String rule);
}




public class Parser
             extends BaseParser
             implements ReduceListener
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

ReduceListener reduceListener = null;
void yyclearin ()       {yychar = (-1);}
void yyerrok ()         {yyerrflag=0;}
void addReduceListener(ReduceListener l) {
  reduceListener = l;}


//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short BOOL=258;
public final static short INT=259;
public final static short STRING=260;
public final static short CLASS=261;
public final static short NULL=262;
public final static short EXTENDS=263;
public final static short THIS=264;
public final static short WHILE=265;
public final static short FOR=266;
public final static short IF=267;
public final static short ELSE=268;
public final static short RETURN=269;
public final static short BREAK=270;
public final static short NEW=271;
public final static short PRINT=272;
public final static short READ_INTEGER=273;
public final static short READ_LINE=274;
public final static short LITERAL=275;
public final static short IDENTIFIER=276;
public final static short AND=277;
public final static short OR=278;
public final static short STATIC=279;
public final static short INSTANCEOF=280;
public final static short LESS_EQUAL=281;
public final static short GREATER_EQUAL=282;
public final static short EQUAL=283;
public final static short NOT_EQUAL=284;
public final static short PCLONE=285;
public final static short SWITCH=286;
public final static short CASE=287;
public final static short DEFAULT=288;
public final static short REPEAT=289;
public final static short UNTIL=290;
public final static short CONTINUE=291;
public final static short UMINUS=292;
public final static short EMPTY=293;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    2,    6,    6,    7,    7,    7,    9,    9,   10,
   10,    8,    8,   11,   12,   12,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   14,   14,
   14,   27,   27,   24,   24,   26,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   29,   29,   28,   28,   30,   30,
   16,   22,   17,   20,   15,   31,   31,   18,   18,   19,
   21,   32,   32,   34,   33,   33,   23,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    2,
    3,    6,    2,    0,    2,    2,    0,    1,    0,    3,
    1,    7,    6,    3,    2,    0,    1,    2,    1,    1,
    1,    2,    2,    2,    1,    1,    2,    2,    3,    1,
    0,    2,    0,    2,    4,    5,    1,    1,    1,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    2,    2,    3,    3,    1,    4,    5,
    6,    5,    5,    3,    1,    1,    1,    0,    3,    1,
    5,    6,    9,    1,    6,    2,    0,    2,    1,    4,
    8,    2,    0,    4,    3,    0,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   13,   17,
    0,    7,    8,    6,    9,    0,    0,   12,   15,    0,
    0,   16,   10,    0,    4,    0,    0,    0,    0,   11,
    0,   21,    0,    0,    0,    0,    5,    0,    0,    0,
   26,   23,   20,   22,    0,   76,   68,    0,    0,    0,
    0,   84,    0,    0,    0,    0,   75,    0,    0,    0,
   97,    0,    0,    0,   24,   27,   35,   25,    0,   29,
   30,   31,    0,    0,    0,   36,    0,    0,    0,    0,
    0,    0,   49,    0,    0,    0,   47,    0,   48,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   28,   32,   33,   34,   37,   38,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   42,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   66,   67,    0,    0,    0,    0,   63,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   69,    0,    0,   90,    0,    0,    0,    0,   45,
    0,    0,    0,   81,    0,    0,   70,    0,    0,   93,
    0,   72,    0,   46,    0,    0,   85,   71,    0,   82,
    0,   86,    0,    0,    0,   92,    0,    0,   26,   91,
   83,   26,    0,    0,
};
final static short yydgoto[] = {                          2,
    3,    4,   66,   20,   33,    8,   11,   22,   34,   35,
   67,   45,   68,   69,   70,   71,   72,   73,   74,   75,
   76,   77,   78,   87,   80,   89,   82,  172,   83,  133,
  187,  189,  195,  196,
};
final static short yysindex[] = {                      -245,
 -251,    0, -245,    0, -222,    0, -233,  -75,    0,    0,
  -80,    0,    0,    0,    0, -210, -127,    0,    0,    8,
  -89,    0,    0,  -85,    0,   39,  -22,   40, -127,    0,
 -127,    0,  -64,   43,   44,   46,    0,  -19, -127,  -19,
    0,    0,    0,    0,    2,    0,    0,   51,   62,   67,
  655,    0, -102,   68,   69,   77,    0,   88,   96,   37,
    0,  655,  655,  456,    0,    0,    0,    0,   60,    0,
    0,    0,   81,   87,   94,    0,  102,  103,   86,  616,
    0, -113,    0,  655,  655,  655,    0,  616,    0,  124,
   80,  655,  127,  132,  655,  655, -115,  -37,  -37, -100,
  336,    0,    0,    0,    0,    0,    0,  655,  655,  655,
  655,  655,  655,  655,  655,  655,  655,  655,  655,  655,
  655,  655,    0,  655,  655,  142,  409,  125,  420,  144,
  640,  616,    5,    0,    0,  431,  461,  143,  145,    0,
  616,  796,  789,   15,   15,  -32,  -32,  763,   27,   27,
  -37,  -37,  -37,   15,   15,  472,  483,  655,   37,  655,
   37,    0,  513,  655,    0,  -87,   85,  655,  655,    0,
  655,  149,  171,    0,  535,  -52,    0,  616,  163,    0,
  586,    0,  616,    0,  655,   37,    0,    0, -232,    0,
  176,    0, -254,  172,   93,    0,   37,  177,    0,    0,
    0,    0,   37,   37,
};
final static short yyrindex[] = {                         0,
    0,    0,  221,    0,  147,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  206,    0,    0,  237,    0,
  237,    0,    0,    0,  240,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -30,    0,    0,    0,    0,    0,
  -28,    0,    0,    0,    0,    0,    0,    0,    0,  -30,
    0,    7,    7,    7,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  739,    0,
  160,    0,    0,    7,  -30,    7,    0,  225,    0,    0,
    0,    7,    0,    0,    7,    7,    0,   79,  107,    0,
    0,    0,    0,    0,    0,    0,    0,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    0,    7,    7,   52,    0,    0,    0,    0,
    7,   24,    0,    0,    0,    0,    0,    0,    0,    0,
  -21,   42,  938,  606,  729,  911,  936,  -40,  816,  880,
  151,  307,  360,  890,  900,    0,    0,  -24,  -30,    7,
  -30,    0,    0,    7,    0,    0,    0,    7,    7,    0,
    7,    0,  244,    0,    0,  -33,    0,   31,    0,    0,
    0,    0,   -8,    0,    3,  -30,    0,    0,  161,    0,
    0,    0,    0,    0,    0,    0,  -30,    0,    0,    0,
    0,    0,    4,  281,
};
final static short yygindex[] = {                         0,
    0,  284,  278,   -7,   23,    0,    0,    0,  259,    0,
   -1, -121,  351,  -82,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  906, 1136,  926,    0,    0,   99,  155,
    0,    0,    0,    0,
};
final static int YYTABLESIZE=1307;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         87,
   74,   27,  128,   74,  120,   27,   87,   46,  123,  118,
  116,   87,  117,  123,  119,    1,   78,   74,   74,   39,
   57,   32,   74,   32,    5,   87,   27,  122,   41,  121,
   89,   43,   73,   21,   63,   73,   42,   39,   44,   24,
    7,   64,    9,   41,   18,  165,   62,   10,  164,   73,
   73,  120,   74,  124,  193,  194,  118,  116,  124,  117,
  123,  119,   41,  120,   80,   23,   25,   80,  118,   63,
   30,   79,  123,  119,   79,   91,   64,  203,   29,   31,
  204,   62,   61,   38,   73,   61,   40,   39,   44,   87,
   84,   87,   44,   44,   44,   44,   44,   44,   44,   61,
   61,   85,  191,   41,   61,  124,   86,   92,   93,   44,
   44,   44,   44,   44,   44,   64,   94,  124,  102,   64,
   64,   64,   64,   64,   41,   64,   65,   95,   95,   12,
   13,   14,   15,   16,   61,   96,   64,   64,   64,  103,
   64,   64,   44,   65,   44,  104,  108,   65,   65,   65,
   65,   65,  105,   65,   12,   13,   14,   15,   16,   41,
  106,  107,  126,  130,   65,   65,   65,  134,   65,   65,
  131,   64,  135,   90,  138,  139,   12,   13,   14,   15,
   16,  158,  168,  160,  162,  169,   26,   52,  179,  184,
   28,   52,   52,   52,   52,   52,   48,   52,   17,   65,
   40,   48,   48,  188,   48,   48,   48,  180,   52,   52,
   52,   37,   52,   52,  164,  186,  197,  200,   40,   48,
    1,   48,   48,   87,   87,   87,   87,   87,   87,  199,
   87,   87,   87,   87,  202,   87,   87,   87,   87,   87,
   87,   87,   87,   52,   74,   43,   87,   43,  111,  112,
   48,   43,   87,   87,   87,   87,   87,   87,   12,   13,
   14,   15,   16,   46,    5,   47,   48,   49,   50,   14,
   51,   52,   53,   54,   55,   56,   57,   19,   43,   43,
   18,   58,   43,   88,   77,   96,    6,   59,   19,   36,
   60,  198,   61,   12,   13,   14,   15,   16,   46,    0,
   47,   48,   49,   50,    0,   51,   52,   53,   54,   55,
   56,   57,  173,    0,    0,    0,   58,    0,   61,   61,
    0,    0,   59,    0,    0,   60,   61,   61,   44,   44,
    0,    0,   44,   44,   44,   44,   44,    0,    0,   41,
    0,    0,    0,   53,    0,    0,    0,   53,   53,   53,
   53,   53,    0,   53,    0,   64,   64,    0,    0,   64,
   64,   64,   64,   64,   53,   53,   53,    0,   53,   53,
    0,    0,  120,    0,    0,    0,  140,  118,  116,    0,
  117,  123,  119,   65,   65,    0,    0,   65,   65,   65,
   65,   65,    0,    0,    0,  122,   54,  121,  125,   53,
   54,   54,   54,   54,   54,   94,   54,    0,    0,    0,
   97,    0,    0,    0,    0,    0,    0,   54,   54,   54,
    0,   54,   54,    0,    0,    0,  124,   52,   52,    0,
    0,   52,   52,   52,   52,   52,   48,   48,    0,    0,
   48,   48,   48,   48,   48,  120,    0,    0,    0,  159,
  118,  116,   54,  117,  123,  119,  120,    0,    0,    0,
  161,  118,  116,    0,  117,  123,  119,  120,  122,    0,
  121,  125,  118,  116,  166,  117,  123,  119,    0,  122,
    0,  121,  125,    0,    0,    0,    0,    0,   63,    0,
  122,    0,  121,  125,    0,   64,    0,  120,    0,  124,
   62,  167,  118,  116,    0,  117,  123,  119,  120,  174,
  124,  176,    0,  118,  116,    0,  117,  123,  119,  120,
  122,  124,  121,  125,  118,  116,    0,  117,  123,  119,
    0,  122,    0,  121,  125,    0,  192,    0,    0,    0,
  171,    0,  122,    0,  121,  125,    0,  201,    0,  120,
    0,  124,    0,    0,  118,  116,   43,  117,  123,  119,
    0,    0,  124,    0,  170,    0,    0,   94,   94,    0,
    0,  120,  122,  124,  121,  125,  118,  116,    0,  117,
  123,  119,    0,   53,   53,    0,    0,   53,   53,   53,
   53,   53,    0,  185,  122,    0,  121,  125,    0,    0,
    0,    0,    0,  124,    0,  177,    0,    0,    0,    0,
    0,    0,  109,  110,    0,    0,  111,  112,  113,  114,
  115,    0,  120,    0,    0,  124,  190,  118,  116,    0,
  117,  123,  119,    0,    0,    0,   54,   54,    0,    0,
   54,   54,   54,   54,   54,  122,   59,  121,  125,   59,
    0,    0,  120,    0,    0,    0,    0,  118,  116,    0,
  117,  123,  119,   59,   59,    0,    0,    0,   59,    0,
    0,    0,   63,    0,    0,  122,  124,  121,  125,   64,
    0,    0,    0,    0,   62,  109,  110,   63,    0,  111,
  112,  113,  114,  115,   64,    0,  109,  110,   59,   62,
  111,  112,  113,  114,  115,    0,  124,  109,  110,    0,
    0,  111,  112,  113,  114,  115,  100,   46,    0,   47,
    0,    0,    0,    0,    0,    0,   53,    0,   55,   56,
   57,    0,   30,    0,    0,   58,    0,  109,  110,    0,
    0,  111,  112,  113,  114,  115,    0,    0,  109,  110,
    0,    0,  111,  112,  113,  114,  115,    0,    0,  109,
  110,    0,    0,  111,  112,  113,  114,  115,    0,   60,
    0,    0,   60,    0,    0,   47,    0,    0,    0,    0,
   47,   47,    0,   47,   47,   47,   60,   60,    0,  109,
  110,   60,    0,  111,  112,  113,  114,  115,   47,  120,
   47,   47,    0,    0,  118,  116,    0,  117,  123,  119,
    0,  109,  110,    0,    0,  111,  112,  113,  114,  115,
    0,   60,  122,    0,  121,  120,    0,    0,    0,   47,
  118,  116,  120,  117,  123,  119,    0,  118,  116,    0,
  117,  123,  119,    0,    0,    0,    0,    0,  122,    0,
  121,    0,    0,  124,    0,  122,   50,  121,   50,   50,
   50,    0,  109,  110,    0,    0,  111,  112,  113,  114,
  115,    0,    0,   50,   50,   50,    0,   50,   50,  124,
    0,    0,   59,   59,    0,    0,  124,    0,   59,   59,
   59,    0,  109,  110,    0,    0,  111,  112,  113,  114,
  115,   46,    0,   47,    0,    0,    0,    0,   50,    0,
   53,    0,   55,   56,   57,    0,   46,    0,   47,   58,
   51,    0,   51,   51,   51,   53,    0,   55,   56,   57,
   58,    0,    0,   58,   58,    0,    0,   51,   51,   51,
   57,   51,   51,   57,    0,    0,    0,   58,   58,    0,
   79,   55,   58,    0,   55,    0,    0,   57,   57,    0,
    0,    0,   57,    0,    0,   79,    0,    0,   55,   55,
   81,    0,   51,   55,    0,    0,   56,    0,   62,   56,
    0,   62,   58,    0,    0,   81,    0,    0,    0,    0,
   79,    0,   57,   56,   56,   62,   62,    0,   56,    0,
   62,    0,    0,   55,    0,   60,   60,    0,    0,    0,
   81,   60,   60,   60,    0,   47,   47,    0,    0,   47,
   47,   47,   47,   47,    0,    0,    0,    0,   56,    0,
   62,    0,    0,    0,    0,    0,    0,    0,    0,  109,
  110,    0,    0,  111,  112,  113,  114,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   79,  109,   79,    0,    0,  111,
  112,  113,  114,    0,    0,    0,  111,  112,  113,  114,
    0,    0,    0,    0,   81,    0,   81,    0,    0,    0,
   79,   79,   50,   50,    0,    0,   50,   50,   50,   50,
   50,    0,   79,    0,    0,    0,    0,    0,   79,   79,
   81,   81,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   81,    0,    0,    0,    0,    0,   81,   81,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   51,   51,    0,    0,
   51,   51,   51,   51,   51,    0,   58,   58,    0,    0,
    0,    0,   58,   58,   58,    0,   57,   57,    0,    0,
    0,    0,   57,   57,   57,    0,   88,   55,   55,    0,
    0,    0,    0,    0,    0,   55,    0,   98,   99,  101,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   56,   56,    0,   62,    0,    0,    0,  127,
   56,  129,   62,    0,    0,    0,    0,  132,    0,    0,
  136,  137,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  141,  142,  143,  144,  145,  146,  147,
  148,  149,  150,  151,  152,  153,  154,  155,    0,  156,
  157,    0,    0,    0,    0,    0,  163,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  132,    0,  175,    0,    0,    0,  178,
    0,    0,    0,  181,  182,    0,  183,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   41,   91,   85,   44,   37,   91,   40,  262,   46,   42,
   43,   45,   45,   46,   47,  261,   41,   58,   59,   41,
  275,   29,   63,   31,  276,   59,   91,   60,   59,   62,
   59,   39,   41,   11,   33,   44,   38,   59,   40,   17,
  263,   40,  276,   41,  125,   41,   45,  123,   44,   58,
   59,   37,   93,   91,  287,  288,   42,   43,   91,   45,
   46,   47,   59,   37,   41,  276,   59,   44,   42,   33,
   93,   41,   46,   47,   44,   53,   40,  199,   40,   40,
  202,   45,   41,   41,   93,   44,   41,   44,   37,  123,
   40,  125,   41,   42,   43,   44,   45,   46,   47,   58,
   59,   40,  185,  123,   63,   91,   40,   40,   40,   58,
   59,   60,   61,   62,   63,   37,   40,   91,   59,   41,
   42,   43,   44,   45,  123,   47,  125,   40,  125,  257,
  258,  259,  260,  261,   93,   40,   58,   59,   60,   59,
   62,   63,   91,   37,   93,   59,   61,   41,   42,   43,
   44,   45,   59,   47,  257,  258,  259,  260,  261,  123,
   59,   59,  276,   40,   58,   59,   60,   41,   62,   63,
   91,   93,   41,  276,  290,  276,  257,  258,  259,  260,
  261,   40,   40,   59,   41,   41,  276,   37,  276,   41,
  276,   41,   42,   43,   44,   45,   37,   47,  279,   93,
   41,   42,   43,   41,   45,   46,   47,  123,   58,   59,
   60,  276,   62,   63,   44,  268,   41,  125,   59,   60,
    0,   62,   63,  257,  258,  259,  260,  261,  262,   58,
  264,  265,  266,  267,   58,  269,  270,  271,  272,  273,
  274,  275,  276,   93,  285,  276,  280,  276,  281,  282,
   91,  276,  286,  287,  288,  289,  290,  291,  257,  258,
  259,  260,  261,  262,   59,  264,  265,  266,  267,  123,
  269,  270,  271,  272,  273,  274,  275,   41,  276,  276,
   41,  280,  276,   59,   41,  125,    3,  286,   11,   31,
  289,  193,  291,  257,  258,  259,  260,  261,  262,   -1,
  264,  265,  266,  267,   -1,  269,  270,  271,  272,  273,
  274,  275,  158,   -1,   -1,   -1,  280,   -1,  277,  278,
   -1,   -1,  286,   -1,   -1,  289,  285,  291,  277,  278,
   -1,   -1,  281,  282,  283,  284,  285,   -1,   -1,   59,
   -1,   -1,   -1,   37,   -1,   -1,   -1,   41,   42,   43,
   44,   45,   -1,   47,   -1,  277,  278,   -1,   -1,  281,
  282,  283,  284,  285,   58,   59,   60,   -1,   62,   63,
   -1,   -1,   37,   -1,   -1,   -1,   41,   42,   43,   -1,
   45,   46,   47,  277,  278,   -1,   -1,  281,  282,  283,
  284,  285,   -1,   -1,   -1,   60,   37,   62,   63,   93,
   41,   42,   43,   44,   45,  125,   47,   -1,   -1,   -1,
   60,   -1,   -1,   -1,   -1,   -1,   -1,   58,   59,   60,
   -1,   62,   63,   -1,   -1,   -1,   91,  277,  278,   -1,
   -1,  281,  282,  283,  284,  285,  277,  278,   -1,   -1,
  281,  282,  283,  284,  285,   37,   -1,   -1,   -1,   41,
   42,   43,   93,   45,   46,   47,   37,   -1,   -1,   -1,
   41,   42,   43,   -1,   45,   46,   47,   37,   60,   -1,
   62,   63,   42,   43,   44,   45,   46,   47,   -1,   60,
   -1,   62,   63,   -1,   -1,   -1,   -1,   -1,   33,   -1,
   60,   -1,   62,   63,   -1,   40,   -1,   37,   -1,   91,
   45,   41,   42,   43,   -1,   45,   46,   47,   37,  159,
   91,  161,   -1,   42,   43,   -1,   45,   46,   47,   37,
   60,   91,   62,   63,   42,   43,   -1,   45,   46,   47,
   -1,   60,   -1,   62,   63,   -1,  186,   -1,   -1,   -1,
   58,   -1,   60,   -1,   62,   63,   -1,  197,   -1,   37,
   -1,   91,   -1,   -1,   42,   43,  276,   45,   46,   47,
   -1,   -1,   91,   -1,   93,   -1,   -1,  287,  288,   -1,
   -1,   37,   60,   91,   62,   63,   42,   43,   -1,   45,
   46,   47,   -1,  277,  278,   -1,   -1,  281,  282,  283,
  284,  285,   -1,   59,   60,   -1,   62,   63,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,  277,  278,   -1,   -1,  281,  282,  283,  284,
  285,   -1,   37,   -1,   -1,   91,   41,   42,   43,   -1,
   45,   46,   47,   -1,   -1,   -1,  277,  278,   -1,   -1,
  281,  282,  283,  284,  285,   60,   41,   62,   63,   44,
   -1,   -1,   37,   -1,   -1,   -1,   -1,   42,   43,   -1,
   45,   46,   47,   58,   59,   -1,   -1,   -1,   63,   -1,
   -1,   -1,   33,   -1,   -1,   60,   91,   62,   63,   40,
   -1,   -1,   -1,   -1,   45,  277,  278,   33,   -1,  281,
  282,  283,  284,  285,   40,   -1,  277,  278,   93,   45,
  281,  282,  283,  284,  285,   -1,   91,  277,  278,   -1,
   -1,  281,  282,  283,  284,  285,  261,  262,   -1,  264,
   -1,   -1,   -1,   -1,   -1,   -1,  271,   -1,  273,  274,
  275,   -1,   93,   -1,   -1,  280,   -1,  277,  278,   -1,
   -1,  281,  282,  283,  284,  285,   -1,   -1,  277,  278,
   -1,   -1,  281,  282,  283,  284,  285,   -1,   -1,  277,
  278,   -1,   -1,  281,  282,  283,  284,  285,   -1,   41,
   -1,   -1,   44,   -1,   -1,   37,   -1,   -1,   -1,   -1,
   42,   43,   -1,   45,   46,   47,   58,   59,   -1,  277,
  278,   63,   -1,  281,  282,  283,  284,  285,   60,   37,
   62,   63,   -1,   -1,   42,   43,   -1,   45,   46,   47,
   -1,  277,  278,   -1,   -1,  281,  282,  283,  284,  285,
   -1,   93,   60,   -1,   62,   37,   -1,   -1,   -1,   91,
   42,   43,   37,   45,   46,   47,   -1,   42,   43,   -1,
   45,   46,   47,   -1,   -1,   -1,   -1,   -1,   60,   -1,
   62,   -1,   -1,   91,   -1,   60,   41,   62,   43,   44,
   45,   -1,  277,  278,   -1,   -1,  281,  282,  283,  284,
  285,   -1,   -1,   58,   59,   60,   -1,   62,   63,   91,
   -1,   -1,  277,  278,   -1,   -1,   91,   -1,  283,  284,
  285,   -1,  277,  278,   -1,   -1,  281,  282,  283,  284,
  285,  262,   -1,  264,   -1,   -1,   -1,   -1,   93,   -1,
  271,   -1,  273,  274,  275,   -1,  262,   -1,  264,  280,
   41,   -1,   43,   44,   45,  271,   -1,  273,  274,  275,
   41,   -1,   -1,   44,  280,   -1,   -1,   58,   59,   60,
   41,   62,   63,   44,   -1,   -1,   -1,   58,   59,   -1,
   45,   41,   63,   -1,   44,   -1,   -1,   58,   59,   -1,
   -1,   -1,   63,   -1,   -1,   60,   -1,   -1,   58,   59,
   45,   -1,   93,   63,   -1,   -1,   41,   -1,   41,   44,
   -1,   44,   93,   -1,   -1,   60,   -1,   -1,   -1,   -1,
   85,   -1,   93,   58,   59,   58,   59,   -1,   63,   -1,
   63,   -1,   -1,   93,   -1,  277,  278,   -1,   -1,   -1,
   85,  283,  284,  285,   -1,  277,  278,   -1,   -1,  281,
  282,  283,  284,  285,   -1,   -1,   -1,   -1,   93,   -1,
   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  277,
  278,   -1,   -1,  281,  282,  283,  284,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  159,  277,  161,   -1,   -1,  281,
  282,  283,  284,   -1,   -1,   -1,  281,  282,  283,  284,
   -1,   -1,   -1,   -1,  159,   -1,  161,   -1,   -1,   -1,
  185,  186,  277,  278,   -1,   -1,  281,  282,  283,  284,
  285,   -1,  197,   -1,   -1,   -1,   -1,   -1,  203,  204,
  185,  186,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  197,   -1,   -1,   -1,   -1,   -1,  203,  204,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  277,  278,   -1,   -1,
  281,  282,  283,  284,  285,   -1,  277,  278,   -1,   -1,
   -1,   -1,  283,  284,  285,   -1,  277,  278,   -1,   -1,
   -1,   -1,  283,  284,  285,   -1,   51,  277,  278,   -1,
   -1,   -1,   -1,   -1,   -1,  285,   -1,   62,   63,   64,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  277,  278,   -1,  278,   -1,   -1,   -1,   84,
  285,   86,  285,   -1,   -1,   -1,   -1,   92,   -1,   -1,
   95,   96,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  108,  109,  110,  111,  112,  113,  114,
  115,  116,  117,  118,  119,  120,  121,  122,   -1,  124,
  125,   -1,   -1,   -1,   -1,   -1,  131,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  158,   -1,  160,   -1,   -1,   -1,  164,
   -1,   -1,   -1,  168,  169,   -1,  171,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=293;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'","'?'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","BOOL","INT","STRING",
"CLASS","NULL","EXTENDS","THIS","WHILE","FOR","IF","ELSE","RETURN","BREAK",
"NEW","PRINT","READ_INTEGER","READ_LINE","LITERAL","IDENTIFIER","AND","OR",
"STATIC","INSTANCEOF","LESS_EQUAL","GREATER_EQUAL","EQUAL","NOT_EQUAL","PCLONE",
"SWITCH","CASE","DEFAULT","REPEAT","UNTIL","CONTINUE","UMINUS","EMPTY",
};
final static String yyrule[] = {
"$accept : Program",
"Program : ClassList",
"ClassList : ClassList ClassDef",
"ClassList : ClassDef",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : VOID",
"Type : BOOL",
"Type : STRING",
"Type : CLASS IDENTIFIER",
"Type : Type '[' ']'",
"ClassDef : CLASS IDENTIFIER ExtendsClause '{' FieldList '}'",
"ExtendsClause : EXTENDS IDENTIFIER",
"ExtendsClause :",
"FieldList : FieldList VariableDef",
"FieldList : FieldList FunctionDef",
"FieldList :",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
"FunctionDef : STATIC Type IDENTIFIER '(' Formals ')' StmtBlock",
"FunctionDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : VariableDef",
"Stmt : SimpleStmt ';'",
"Stmt : IfStmt",
"Stmt : WhileStmt",
"Stmt : ForStmt",
"Stmt : ReturnStmt ';'",
"Stmt : PrintStmt ';'",
"Stmt : BreakStmt ';'",
"Stmt : StmtBlock",
"Stmt : SwitchStmt",
"Stmt : RepeatStmt ';'",
"Stmt : ContinueStmt ';'",
"SimpleStmt : LValue '=' Expr",
"SimpleStmt : Call",
"SimpleStmt :",
"Receiver : Expr '.'",
"Receiver :",
"LValue : Receiver IDENTIFIER",
"LValue : Expr '[' Expr ']'",
"Call : Receiver IDENTIFIER '(' Actuals ')'",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr '<' Expr",
"Expr : Expr '>' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '(' Expr ')'",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Expr : READ_INTEGER '(' ')'",
"Expr : READ_LINE '(' ')'",
"Expr : THIS",
"Expr : NEW IDENTIFIER '(' ')'",
"Expr : NEW Type '[' Expr ']'",
"Expr : INSTANCEOF '(' Expr ',' IDENTIFIER ')'",
"Expr : '(' CLASS IDENTIFIER ')' Expr",
"Expr : Expr '?' Expr ':' Expr",
"Expr : Expr PCLONE Expr",
"Constant : LITERAL",
"Constant : NULL",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"RepeatStmt : REPEAT Stmt UNTIL '(' Expr ')'",
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
"SwitchStmt : SWITCH '(' Expr ')' '{' CaseList DefaultStmt '}'",
"CaseList : CaseList CaseStmt",
"CaseList :",
"CaseStmt : CASE Constant ':' StmtList",
"DefaultStmt : DEFAULT ':' StmtList",
"DefaultStmt :",
"ContinueStmt : CONTINUE",
};

//#line 486 "Parser.y"
    
	/**
	 * 打印当前归约所用的语法规则<br>
	 * 请勿修改。
	 */
    public boolean onReduce(String rule) {
		if (rule.startsWith("$$"))
			return false;
		else
			rule = rule.replaceAll(" \\$\\$\\d+", "");

   	    if (rule.endsWith(":"))
    	    System.out.println(rule + " <empty>");
   	    else
			System.out.println(rule);
		return false;
    }
    
    public void diagnose() {
		addReduceListener(this);
		yyparse();
	}
//#line 671 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0 || valptr<0)   //check for under & overflow here
            {
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0 || valptr<0)   //check for under & overflow here
              {
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    if (reduceListener == null || reduceListener.onReduce(yyrule[yyn])) // if intercepted!
      switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 59 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 65 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 69 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 79 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 85 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 89 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 93 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 9:
//#line 97 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 10:
//#line 101 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 11:
//#line 105 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 12:
//#line 111 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 13:
//#line 117 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 14:
//#line 121 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 15:
//#line 127 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 16:
//#line 131 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 17:
//#line 135 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 19:
//#line 143 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 20:
//#line 150 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 21:
//#line 154 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 22:
//#line 161 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 23:
//#line 165 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 171 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 25:
//#line 177 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 26:
//#line 181 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 27:
//#line 188 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 28:
//#line 193 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 39:
//#line 211 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 40:
//#line 215 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 41:
//#line 219 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 43:
//#line 226 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 44:
//#line 232 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 45:
//#line 239 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 46:
//#line 245 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 47:
//#line 254 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 50:
//#line 260 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 51:
//#line 264 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 52:
//#line 268 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 53:
//#line 272 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 54:
//#line 276 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 280 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 284 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 288 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 292 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 296 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 300 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 61:
//#line 304 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 308 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 63:
//#line 312 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 64:
//#line 316 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 65:
//#line 320 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 66:
//#line 324 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 67:
//#line 328 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 68:
//#line 332 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 69:
//#line 336 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 70:
//#line 340 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 71:
//#line 344 "Parser.y"
{
                		yyval.expr = new Tree.TypeTest(val_peek(3).expr, val_peek(1).ident, val_peek(5).loc);
                	}
break;
case 72:
//#line 348 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 73:
//#line 352 "Parser.y"
{
                		yyval.expr = new Tree.Ternary(Tree.COND, val_peek(4).expr, val_peek(2).expr, val_peek(0).expr, val_peek(4).loc);
                	}
break;
case 74:
//#line 356 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PCLONE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 75:
//#line 362 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 76:
//#line 366 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 78:
//#line 373 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 79:
//#line 380 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 80:
//#line 384 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 81:
//#line 391 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 82:
//#line 397 "Parser.y"
{
						yyval.stmt = new Tree.Repeat(val_peek(4).stmt, val_peek(1).expr, val_peek(5).loc);
					}
break;
case 83:
//#line 403 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 84:
//#line 409 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 85:
//#line 415 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 86:
//#line 421 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 87:
//#line 425 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 88:
//#line 431 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 89:
//#line 435 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 90:
//#line 441 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 91:
//#line 447 "Parser.y"
{
						yyval.stmt = new Tree.Switch(val_peek(5).expr, val_peek(2).slist, val_peek(1).stmt, val_peek(7).loc);
					}
break;
case 92:
//#line 453 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 93:
//#line 457 "Parser.y"
{
						yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
					}
break;
case 94:
//#line 464 "Parser.y"
{
						yyval.stmt = new Tree.Case(val_peek(2).expr, val_peek(0).slist, val_peek(3).loc);
					}
break;
case 95:
//#line 470 "Parser.y"
{
						yyval.stmt = new Tree.Default(val_peek(0).slist, val_peek(2).loc);
					}
break;
case 96:
//#line 474 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 97:
//#line 480 "Parser.y"
{
						yyval.stmt = new Tree.Continue(val_peek(0).loc);
					}
break;
//#line 1319 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
