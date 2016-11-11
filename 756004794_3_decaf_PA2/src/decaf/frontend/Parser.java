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
public final static short UMINUS=286;
public final static short EMPTY=287;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    2,    6,    6,    7,    7,    7,    9,    9,   10,
   10,    8,    8,   11,   12,   12,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   14,   14,   14,   24,   24,
   21,   21,   23,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   26,   26,   25,   25,   27,   27,   16,   17,   20,
   15,   28,   28,   18,   18,   19,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    2,
    3,    6,    2,    0,    2,    2,    0,    1,    0,    3,
    1,    7,    6,    3,    2,    0,    1,    2,    1,    1,
    1,    2,    2,    2,    1,    3,    1,    0,    2,    0,
    2,    4,    5,    1,    1,    1,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    2,    2,    3,    3,    1,    4,    5,    6,    5,    5,
    3,    1,    1,    1,    0,    3,    1,    5,    9,    1,
    6,    2,    0,    2,    1,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   13,   17,
    0,    7,    8,    6,    9,    0,    0,   12,   15,    0,
    0,   16,   10,    0,    4,    0,    0,    0,    0,   11,
    0,   21,    0,    0,    0,    0,    5,    0,    0,    0,
   26,   23,   20,   22,    0,   73,   65,    0,    0,    0,
    0,   80,    0,    0,    0,    0,   72,    0,    0,    0,
    0,   24,   27,   35,   25,    0,   29,   30,   31,    0,
    0,    0,    0,    0,    0,    0,   46,    0,    0,    0,
   44,    0,   45,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   28,   32,   33,   34,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   63,   64,    0,    0,   60,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   66,
    0,    0,   86,    0,    0,   42,    0,    0,    0,   78,
    0,    0,   67,    0,    0,   69,    0,   43,    0,    0,
   81,   68,    0,   82,    0,   79,
};
final static short yydgoto[] = {                          2,
    3,    4,   63,   20,   33,    8,   11,   22,   34,   35,
   64,   45,   65,   66,   67,   68,   69,   70,   71,   72,
   81,   74,   83,   76,  158,   77,  123,  171,
};
final static short yysindex[] = {                      -244,
 -258,    0, -244,    0, -235,    0, -246,  -85,    0,    0,
  130,    0,    0,    0,    0, -225,   41,    0,    0,   -4,
  -88,    0,    0,  -78,    0,   24,  -28,   34,   41,    0,
   41,    0,  -77,   25,   31,   50,    0,  -27,   41,  -27,
    0,    0,    0,    0,  386,    0,    0,   60,   61,   87,
  540,    0,   75,   96,   97,  102,    0,  109,  540,  540,
  282,    0,    0,    0,    0,   43,    0,    0,    0,   52,
   56,   79,   91,  478,    0, -123,    0,  540,  540,  540,
    0,  478,    0,  114,   65,  540,  116,  125,  540,  -31,
  -31, -105,   98,    0,    0,    0,    0,  540,  540,  540,
  540,  540,  540,  540,  540,  540,  540,  540,  540,  540,
  540,  540,    0,  540,  540,  132,  122,  118,  133,  142,
  561,  478,  -25,    0,    0,  160,  145,    0,  478,  704,
  649,  250,  250,  731,  731,  625,  215,  215,  -31,  -31,
  -31,  250,  250,  172,  421,  540,  317,  540,  317,    0,
  443,  540,    0, -103,  540,    0,  540,  146,  147,    0,
  454,  -74,    0,  478,  175,    0,  478,    0,  540,  317,
    0,    0,  180,    0,  317,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  201,    0,  104,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  183,    0,    0,  190,    0,
  190,    0,    0,    0,  198,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -48,    0,    0,    0,    0,    0,
  -47,    0,    0,    0,    0,    0,    0,    0,  -33,  -33,
  -33,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  505,    0,   71,    0,    0,  -33,  -48,  -33,
    0,  191,    0,    0,    0,  -33,    0,    0,  -33,  -10,
   -1,    0,    0,    0,    0,    0,    0,  -33,  -33,  -33,
  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,
  -33,  -33,    0,  -33,  -33,  -37,    0,    0,    0,    0,
  -33,   46,    0,    0,    0,    0,    0,    0,  -12,  828,
  878,   88,  740,  841,  853,  167,  785,  808,   26,   35,
   62,  796,  817,    0,    0,  -40,  -48,  -33,  -48,    0,
    0,  -33,    0,    0,  -33,    0,  -33,    0,  208,    0,
    0,  353,    0,   82,    0,    0,  389,    0,  -39,  -48,
    0,    0,    0,    0,  -48,    0,
};
final static short yygindex[] = {                         0,
    0,  251,  242,  161,   28,    0,    0,    0,  225,    0,
  110,    0,   63,  -59,    0,    0,    0,    0,    0,    0,
  323, 1073,  805,    0,    0,    0,  112,    0,
};
final static int YYTABLESIZE=1230;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         41,
   75,   38,   27,   41,   41,   41,   41,   41,   41,   41,
   38,   85,   27,   27,  113,  153,    1,    5,  152,  118,
   41,   41,   41,   41,   41,   41,   61,    7,   36,    9,
   61,   61,   61,   61,   61,   62,   61,   10,   21,   62,
   62,   62,   62,   62,   24,   62,   36,   61,   61,   61,
   23,   61,   61,   41,   25,   41,   62,   62,   62,  114,
   62,   62,   49,   29,   30,   38,   49,   49,   49,   49,
   49,   50,   49,   31,   39,   50,   50,   50,   50,   50,
   85,   50,   61,   49,   49,   49,   77,   49,   49,   77,
   40,   62,   50,   50,   50,   41,   50,   50,   51,   78,
   79,   94,   51,   51,   51,   51,   51,   45,   51,  173,
   95,   37,   45,   45,   96,   45,   45,   45,   49,   51,
   51,   51,   76,   51,   51,   76,   80,   50,   56,   37,
   45,   56,   45,   45,  110,   86,   87,   97,  128,  108,
  106,   88,  107,  113,  109,   56,   56,   42,   89,   44,
   56,   98,  116,  120,   51,  121,  124,  112,  110,  111,
  115,   45,  147,  108,  106,  125,  107,  113,  109,  110,
  127,  146,  165,  149,  108,  106,  148,  107,  113,  109,
   56,  112,  150,  111,  115,  155,  168,   26,  114,   32,
  152,   32,  112,  170,  111,  115,  110,   28,   37,   43,
    1,  108,  106,  154,  107,  113,  109,   71,  110,  160,
   71,  162,  114,  108,  106,  172,  107,  113,  109,  112,
  175,  111,  115,  114,   71,   71,   14,   40,   40,   71,
   19,  112,  174,  111,  115,   40,   40,  176,   18,   41,
   41,    5,   40,   41,   41,   41,   41,   41,   74,   84,
  114,  110,   19,    6,   18,   36,  108,  159,    0,   71,
  113,  109,  114,    0,  156,    0,   61,   61,    0,    0,
   61,   61,   61,   61,   61,   62,   62,    0,    0,   62,
   62,   62,   62,   62,    0,    0,  110,    0,    0,    0,
    0,  108,  106,    0,  107,  113,  109,   12,   13,   14,
   15,   16,   49,   49,    0,  114,   49,   49,   49,   49,
   49,   50,   50,    0,   60,   50,   50,   50,   50,   50,
    0,   61,    0,    0,    0,    0,   59,    0,    0,    0,
    0,   12,   13,   14,   15,   16,    0,    0,   51,   51,
  114,    0,   51,   51,   51,   51,   51,   45,   45,   60,
   84,   45,   45,   45,   45,   45,   61,    0,    0,    0,
    0,   59,    0,    0,   56,   56,    0,   73,    0,    0,
   56,   56,   56,    0,   99,  100,    0,    0,  101,  102,
  103,  104,  105,    0,    0,   83,   12,   13,   14,   15,
   16,    0,   83,    0,    0,    0,    0,   83,   99,  100,
    0,   73,  101,  102,  103,  104,  105,    0,   17,   99,
  100,   83,    0,  101,  102,  103,  104,  105,   60,    0,
    0,    0,    0,    0,    0,   61,    0,    0,    0,   70,
   59,    0,   70,    0,    0,    0,   99,  100,    0,   41,
  101,  102,  103,  104,  105,    0,   70,   70,   99,  100,
    0,   71,  101,  102,  103,  104,  105,  110,    0,    0,
    0,    0,  108,  106,    0,  107,  113,  109,    0,   73,
    0,   73,    0,    0,    0,   83,    0,   83,  157,  110,
  112,   70,  111,  115,  108,  106,    0,  107,  113,  109,
  110,   73,   73,    0,    0,  108,  106,   73,  107,  113,
  109,    0,  112,    0,  111,  115,    0,    0,   41,    0,
   62,  114,  169,  112,  110,  111,  115,    0,    0,  108,
  106,    0,  107,  113,  109,    0,    0,    0,    0,    0,
    0,    0,    0,  114,    0,  163,    0,  112,    0,  111,
  115,   44,   92,   46,  114,   47,   44,   44,    0,   44,
   44,   44,   53,    0,   55,   56,   57,    0,    0,    0,
    0,   58,    0,    0,   44,    0,   44,   44,  114,    0,
    0,    0,   60,   12,   13,   14,   15,   16,   46,   61,
   47,   48,   49,   50,   59,   51,   52,   53,   54,   55,
   56,   57,    0,   60,    0,   44,   58,    0,    0,    0,
   61,    0,    0,    0,    0,   59,    0,    0,    0,   83,
   83,   83,   83,   83,   83,    0,   83,   83,   83,   83,
    0,   83,   83,   83,   83,   83,   83,   83,   83,    0,
    0,    0,   83,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   12,   13,   14,   15,   16,   46,    0,   47,
   48,   49,   50,   30,   51,   52,   53,   54,   55,   56,
   57,  110,    0,    0,    0,   58,  108,  106,    0,  107,
  113,  109,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  112,  110,  111,    0,    0,    0,
  108,  106,    0,  107,  113,  109,    0,   99,  100,    0,
    0,  101,  102,  103,  104,  105,    0,    0,  112,    0,
  111,    0,    0,    0,    0,  114,    0,    0,    0,   99,
  100,    0,    0,  101,  102,  103,  104,  105,    0,    0,
   99,  100,    0,    0,  101,  102,  103,  104,  105,  114,
  110,    0,    0,    0,    0,  108,  106,    0,  107,  113,
  109,    0,    0,    0,   99,  100,    0,    0,  101,  102,
  103,  104,  105,  112,    0,  111,    0,  110,    0,    0,
    0,    0,  108,  106,    0,  107,  113,  109,    0,    0,
   57,   44,   44,   57,    0,   44,   44,   44,   44,   44,
  112,    0,  111,    0,  114,    0,    0,   57,   57,    0,
    0,   46,   57,   47,    0,    0,    0,    0,    0,    0,
   53,    0,   55,   56,   57,    0,    0,    0,    0,   58,
    0,  114,   46,    0,   47,   47,    0,   47,   47,   47,
    0,   53,   57,   55,   56,   57,   55,    0,    0,   55,
   58,    0,   47,   47,   47,    0,   47,   47,   48,   75,
   48,   48,   48,   55,   55,    0,    0,   54,   55,    0,
   54,    0,    0,    0,    0,   48,   48,   48,   58,   48,
   48,   58,    0,    0,   54,   54,    0,   47,    0,   54,
    0,   52,    0,   75,   52,   58,   58,    0,   55,    0,
   58,    0,    0,   53,    0,    0,   53,    0,   52,   52,
   48,   99,  100,   52,    0,  101,  102,  103,  104,   54,
   53,   53,    0,    0,    0,   53,    0,    0,   59,    0,
   58,   59,    0,    0,    0,   99,    0,    0,    0,  101,
  102,  103,  104,   52,    0,   59,   59,    0,    0,    0,
   59,    0,    0,    0,    0,   53,    0,    0,    0,    0,
    0,   75,    0,   75,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   59,    0,    0,   75,   75,    0,    0,    0,    0,   75,
    0,    0,    0,    0,  101,  102,  103,  104,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  101,  102,    0,    0,    0,   57,   57,    0,    0,
    0,    0,   57,   57,   57,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   47,   47,    0,    0,   47,   47,   47,   47,   47,
    0,    0,   55,   55,    0,    0,    0,    0,   55,   55,
   55,    0,    0,    0,   48,   48,    0,    0,   48,   48,
   48,   48,   48,   54,   54,    0,    0,    0,    0,   54,
   54,   54,    0,    0,   58,   58,    0,    0,    0,    0,
    0,    0,   58,    0,    0,    0,    0,   52,   52,    0,
    0,    0,    0,   82,    0,   52,    0,    0,    0,   53,
   53,   90,   91,   93,    0,    0,    0,   53,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  117,    0,  119,    0,    0,   59,    0,    0,  122,    0,
    0,  126,   59,    0,    0,    0,    0,    0,    0,    0,
  129,  130,  131,  132,  133,  134,  135,  136,  137,  138,
  139,  140,  141,  142,  143,    0,  144,  145,    0,    0,
    0,    0,    0,  151,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  122,    0,
  161,    0,    0,    0,  164,    0,    0,  166,    0,  167,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         37,
   41,   41,   91,   41,   42,   43,   44,   45,   46,   47,
   59,   59,   91,   91,   46,   41,  261,  276,   44,   79,
   58,   59,   60,   61,   62,   63,   37,  263,   41,  276,
   41,   42,   43,   44,   45,   37,   47,  123,   11,   41,
   42,   43,   44,   45,   17,   47,   59,   58,   59,   60,
  276,   62,   63,   91,   59,   93,   58,   59,   60,   91,
   62,   63,   37,   40,   93,   41,   41,   42,   43,   44,
   45,   37,   47,   40,   44,   41,   42,   43,   44,   45,
   53,   47,   93,   58,   59,   60,   41,   62,   63,   44,
   41,   93,   58,   59,   60,  123,   62,   63,   37,   40,
   40,   59,   41,   42,   43,   44,   45,   37,   47,  169,
   59,   41,   42,   43,   59,   45,   46,   47,   93,   58,
   59,   60,   41,   62,   63,   44,   40,   93,   41,   59,
   60,   44,   62,   63,   37,   40,   40,   59,   41,   42,
   43,   40,   45,   46,   47,   58,   59,   38,   40,   40,
   63,   61,  276,   40,   93,   91,   41,   60,   37,   62,
   63,   91,   41,   42,   43,   41,   45,   46,   47,   37,
  276,   40,  276,   41,   42,   43,   59,   45,   46,   47,
   93,   60,   41,   62,   63,   41,   41,  276,   91,   29,
   44,   31,   60,  268,   62,   63,   37,  276,  276,   39,
    0,   42,   43,   44,   45,   46,   47,   41,   37,  147,
   44,  149,   91,   42,   43,   41,   45,   46,   47,   60,
   41,   62,   63,   91,   58,   59,  123,  276,  276,   63,
   41,   60,  170,   62,   63,  276,  276,  175,   41,  277,
  278,   59,  276,  281,  282,  283,  284,  285,   41,   59,
   91,   37,   11,    3,  125,   31,   42,  146,   -1,   93,
   46,   47,   91,   -1,   93,   -1,  277,  278,   -1,   -1,
  281,  282,  283,  284,  285,  277,  278,   -1,   -1,  281,
  282,  283,  284,  285,   -1,   -1,   37,   -1,   -1,   -1,
   -1,   42,   43,   -1,   45,   46,   47,  257,  258,  259,
  260,  261,  277,  278,   -1,   91,  281,  282,  283,  284,
  285,  277,  278,   -1,   33,  281,  282,  283,  284,  285,
   -1,   40,   -1,   -1,   -1,   -1,   45,   -1,   -1,   -1,
   -1,  257,  258,  259,  260,  261,   -1,   -1,  277,  278,
   91,   -1,  281,  282,  283,  284,  285,  277,  278,   33,
  276,  281,  282,  283,  284,  285,   40,   -1,   -1,   -1,
   -1,   45,   -1,   -1,  277,  278,   -1,   45,   -1,   -1,
  283,  284,  285,   -1,  277,  278,   -1,   -1,  281,  282,
  283,  284,  285,   -1,   -1,   33,  257,  258,  259,  260,
  261,   -1,   40,   -1,   -1,   -1,   -1,   45,  277,  278,
   -1,   79,  281,  282,  283,  284,  285,   -1,  279,  277,
  278,   59,   -1,  281,  282,  283,  284,  285,   33,   -1,
   -1,   -1,   -1,   -1,   -1,   40,   -1,   -1,   -1,   41,
   45,   -1,   44,   -1,   -1,   -1,  277,  278,   -1,  123,
  281,  282,  283,  284,  285,   -1,   58,   59,  277,  278,
   -1,  285,  281,  282,  283,  284,  285,   37,   -1,   -1,
   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,  147,
   -1,  149,   -1,   -1,   -1,  123,   -1,  125,   58,   37,
   60,   93,   62,   63,   42,   43,   -1,   45,   46,   47,
   37,  169,  170,   -1,   -1,   42,   43,  175,   45,   46,
   47,   -1,   60,   -1,   62,   63,   -1,   -1,  123,   -1,
  125,   91,   59,   60,   37,   62,   63,   -1,   -1,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   -1,   60,   -1,   62,
   63,   37,  261,  262,   91,  264,   42,   43,   -1,   45,
   46,   47,  271,   -1,  273,  274,  275,   -1,   -1,   -1,
   -1,  280,   -1,   -1,   60,   -1,   62,   63,   91,   -1,
   -1,   -1,   33,  257,  258,  259,  260,  261,  262,   40,
  264,  265,  266,  267,   45,  269,  270,  271,  272,  273,
  274,  275,   -1,   33,   -1,   91,  280,   -1,   -1,   -1,
   40,   -1,   -1,   -1,   -1,   45,   -1,   -1,   -1,  257,
  258,  259,  260,  261,  262,   -1,  264,  265,  266,  267,
   -1,  269,  270,  271,  272,  273,  274,  275,  276,   -1,
   -1,   -1,  280,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,  259,  260,  261,  262,   -1,  264,
  265,  266,  267,   93,  269,  270,  271,  272,  273,  274,
  275,   37,   -1,   -1,   -1,  280,   42,   43,   -1,   45,
   46,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   60,   37,   62,   -1,   -1,   -1,
   42,   43,   -1,   45,   46,   47,   -1,  277,  278,   -1,
   -1,  281,  282,  283,  284,  285,   -1,   -1,   60,   -1,
   62,   -1,   -1,   -1,   -1,   91,   -1,   -1,   -1,  277,
  278,   -1,   -1,  281,  282,  283,  284,  285,   -1,   -1,
  277,  278,   -1,   -1,  281,  282,  283,  284,  285,   91,
   37,   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   46,
   47,   -1,   -1,   -1,  277,  278,   -1,   -1,  281,  282,
  283,  284,  285,   60,   -1,   62,   -1,   37,   -1,   -1,
   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   41,  277,  278,   44,   -1,  281,  282,  283,  284,  285,
   60,   -1,   62,   -1,   91,   -1,   -1,   58,   59,   -1,
   -1,  262,   63,  264,   -1,   -1,   -1,   -1,   -1,   -1,
  271,   -1,  273,  274,  275,   -1,   -1,   -1,   -1,  280,
   -1,   91,  262,   -1,  264,   41,   -1,   43,   44,   45,
   -1,  271,   93,  273,  274,  275,   41,   -1,   -1,   44,
  280,   -1,   58,   59,   60,   -1,   62,   63,   41,   45,
   43,   44,   45,   58,   59,   -1,   -1,   41,   63,   -1,
   44,   -1,   -1,   -1,   -1,   58,   59,   60,   41,   62,
   63,   44,   -1,   -1,   58,   59,   -1,   93,   -1,   63,
   -1,   41,   -1,   79,   44,   58,   59,   -1,   93,   -1,
   63,   -1,   -1,   41,   -1,   -1,   44,   -1,   58,   59,
   93,  277,  278,   63,   -1,  281,  282,  283,  284,   93,
   58,   59,   -1,   -1,   -1,   63,   -1,   -1,   41,   -1,
   93,   44,   -1,   -1,   -1,  277,   -1,   -1,   -1,  281,
  282,  283,  284,   93,   -1,   58,   59,   -1,   -1,   -1,
   63,   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,
   -1,  147,   -1,  149,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   93,   -1,   -1,  169,  170,   -1,   -1,   -1,   -1,  175,
   -1,   -1,   -1,   -1,  281,  282,  283,  284,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  281,  282,   -1,   -1,   -1,  277,  278,   -1,   -1,
   -1,   -1,  283,  284,  285,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  277,  278,   -1,   -1,  281,  282,  283,  284,  285,
   -1,   -1,  277,  278,   -1,   -1,   -1,   -1,  283,  284,
  285,   -1,   -1,   -1,  277,  278,   -1,   -1,  281,  282,
  283,  284,  285,  277,  278,   -1,   -1,   -1,   -1,  283,
  284,  285,   -1,   -1,  277,  278,   -1,   -1,   -1,   -1,
   -1,   -1,  285,   -1,   -1,   -1,   -1,  277,  278,   -1,
   -1,   -1,   -1,   51,   -1,  285,   -1,   -1,   -1,  277,
  278,   59,   60,   61,   -1,   -1,   -1,  285,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   78,   -1,   80,   -1,   -1,  278,   -1,   -1,   86,   -1,
   -1,   89,  285,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   98,   99,  100,  101,  102,  103,  104,  105,  106,  107,
  108,  109,  110,  111,  112,   -1,  114,  115,   -1,   -1,
   -1,   -1,   -1,  121,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  146,   -1,
  148,   -1,   -1,   -1,  152,   -1,   -1,  155,   -1,  157,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=287;
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
"UMINUS","EMPTY",
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
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
};

//#line 435 "Parser.y"
    
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
//#line 625 "Parser.java"
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
//#line 56 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 62 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 66 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 76 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 82 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 86 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 90 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 9:
//#line 94 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 10:
//#line 98 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 11:
//#line 102 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 12:
//#line 108 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 13:
//#line 114 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 14:
//#line 118 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 15:
//#line 124 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 16:
//#line 128 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 17:
//#line 132 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 19:
//#line 140 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 20:
//#line 147 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 21:
//#line 151 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 22:
//#line 158 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 23:
//#line 162 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 168 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 25:
//#line 174 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 26:
//#line 178 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 27:
//#line 185 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 28:
//#line 190 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 36:
//#line 205 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 37:
//#line 209 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 38:
//#line 213 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 40:
//#line 220 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 41:
//#line 226 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 42:
//#line 233 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 43:
//#line 239 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 44:
//#line 248 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 47:
//#line 254 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 48:
//#line 258 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 49:
//#line 262 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 50:
//#line 266 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 51:
//#line 270 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 52:
//#line 274 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 53:
//#line 278 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 54:
//#line 282 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 286 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 290 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 294 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 298 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 302 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 306 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 61:
//#line 310 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 314 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 63:
//#line 318 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 64:
//#line 322 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 65:
//#line 326 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 66:
//#line 330 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 67:
//#line 334 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 68:
//#line 338 "Parser.y"
{
                		yyval.expr = new Tree.TypeTest(val_peek(3).expr, val_peek(1).ident, val_peek(5).loc);
                	}
break;
case 69:
//#line 342 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 70:
//#line 346 "Parser.y"
{
                		yyval.expr = new Tree.Ternary(Tree.COND, val_peek(4).expr, val_peek(2).expr, val_peek(0).expr, val_peek(3).loc);
                	}
break;
case 71:
//#line 350 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PCLONE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 72:
//#line 356 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 73:
//#line 360 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 75:
//#line 367 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 76:
//#line 374 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 77:
//#line 378 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 78:
//#line 385 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 79:
//#line 391 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 80:
//#line 397 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 81:
//#line 403 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 82:
//#line 409 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 83:
//#line 413 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 84:
//#line 419 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 85:
//#line 423 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 86:
//#line 429 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
//#line 1224 "Parser.java"
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
