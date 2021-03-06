/**
 * Define a grammar called JMessageBuilder
 */
grammar JMessageBuilder;

@header {
import java.util.Set;
import java.util.HashSet;
}
@parser::members {
	Set<String> types = new HashSet<String>();

	boolean isModule() { return types.contains(getCurrentToken().getText()); }

	boolean isModuleInterface = false;
}


compilationUnit
	:	includeDeclaration* typeDeclaration* EOF
;

includeDeclaration
	:	singleTypeIncludeDeclaration
	;

singleTypeIncludeDeclaration
	:	INCLUDE typeName ONCE ';'
;

typeName
	:	'"' Identifier '.s4h"' { types.add($Identifier.text); }
;

typeDeclaration
//	: 	moduleInterfaceDeclaration
	: 	moduleDeclaration
	| 	adhocModuleBodyDeclaration
	;

moduleDeclaration
	: DECLARE MODULE INTERFACE? moduleIdentifier moduleBody { isModuleInterface = $INTERFACE != null; }
;

adhocModuleBodyDeclaration
	:	adhocModuleMemberDeclaration+
;

moduleBody
	: '{' moduleBodyDeclaration* '}'
;

moduleBodyDeclaration
	:	moduleMemberDeclaration
;

adhocModuleMemberDeclaration
	:	fieldDeclaration
	|	recordTypeDeclaration
	|	procedureDeclaration
	|	procedureCall
//	|	expression
//	|	statement
	|	';'
;

moduleMemberDeclaration
	:	fieldDeclaration
	|	{!isModuleInterface}? functionDeclaration
	|	{!isModuleInterface}? procedureDeclaration
	|	';'
;

//////////////////////////////////////////////////////////////////////
//////////////   FUNCTION  AND STATEMENT ( PROCEDURE )  DECLARATIONS

functionDeclaration
    :   DECLARE PUBLIC? NATIVE? FUNCTION Identifier functionFormalParameters returnType?
        (   methodBody
        |   ';'
        )
;

returnType
	:	PRIMITIVE
	|	arraySpecifier
	|	Identifier
	|	arraySpecifier Identifier
;

functionFormalParameters
    :   '(' functionFormalParameterList? ')'
    ;

functionFormalParameterList
    :   functionFormalParameter (',' functionFormalParameter)*
;

functionFormalParameter
	:	variableIdentifier arraySpecifier? defaultValue? argumentType?
;

procedureDeclaration
    :   DECLARE PUBLIC? STATEMENT START procedureFormalParameters
        (   methodBody	|   ';'  )
    |   DECLARE PUBLIC? STATEMENT Identifier procedureFormalParameters
        (   methodBody	|   ';'  )
;

procedureFormalParameters
    :   procedureFormalParameterList?
    ;

procedureFormalParameterList
	:	procedureFormalParameter (noiseWord procedureFormalParameter)*
;

procedureFormalParameter
	:	inoutSpecifier? variableIdentifier arraySpecifier? procedureDefaultValue? argumentType?
;

noiseWord
	:	COMMA
	|	Identifier
;

inoutSpecifier
	:	IN
	|	OUT
	|	IN OUT
;

procedureDefaultValue
	:	'=' literal
	|	'=' literal DROP
;

arraySpecifier
	:	('[' ']')+
;

defaultValue
	:	'=' literal
;

argumentType
	:	PRIMITIVE
	|	Identifier
;

methodBody
    :   block
;

block
    :   '{' blockStatement* '}'
    ;

blockStatement
    :   fieldDeclaration
    |	callInternalProcedure
    |	procedureCall ';'
    |	assignmentExpression ';'
    |	RETURN ';'
    |	';'
;

///////////////////////////////////////////////////////////////////////////////////
//////////////   PROCEDURE CALLS

procedureCall
//	:	'CALL' expressionName procedureRealParameterList?
	:	START procedureRealParameterList?
	|	SL_STRING_REVERSE procedureRealParameterList?
	|	SOCKET_DICONNECT procedureRealParameterList?
;

procedureRealParameterList
	:	procedureRealParameter (noiseWord procedureRealParameter)*
;

procedureRealParameter
	:	expression
;

START
	:	[Ss] [Tt] [Aa] [Rr] [Tt]
;

SL_STRING_REVERSE
	:	[Ss] [Ll] [_] [Ss] [Tt] [Rr] [Ii] [Nn] [Gg] '.' [Rr] [Ee] [Vv] [Ee] [Rr] [Ss] [Ee]
;

SOCKET_DICONNECT
	:	[Ss] [Oo] [Cc] [Kk] [Ee] [Tt] '.' [Dd] [Ii] [Ss] [Cc] [Oo] [Nn] [Nn] [Ee] [Cc] [Tt];

///////////////////////////////////////////////////////////////////////////////////
//////////////   VARIABLES

fieldDeclaration
	:	DECLARE PUBLIC? VARIABLE? variableDeclarator unannType constOnce? ';'
;

constOnce
	:	CONSTANT
	|	ONCE
;

variableDeclarator
	:	variableDeclaratorId (ASSIGN variableInitializer)?
;

variableDeclaratorId
	:	variableIdentifier dims?
;

dims
	:	Dim (Dim)*
;

Dim
	:	'[' ']'
;

variableInitializer
	:	expression
//	|	arrayInitializer
;

///////////////////////////////////////////////////////////////////////////////////
//////////     RECORDS

recordTypeDeclaration
	:	DECLARE PUBLIC? RECORD recordIdentifier recordBlock
;

recordBlock
	:	'{' recordField* '}'
;

recordField
	:	DECLARE PUBLIC? FIELD? variableIdentifier unannType ';'
;

///////////////////////////////////////////////////////////////////////////////////
///////////    EXPRESSIONS

expression
	:	builtinFunctionCall
	|	primary
//	|	assignmentExpression
	|	functionInvocation
//	|	expression '.' Identifier
//	|	expression '(' expressionList? ')'
;

assignmentExpression
	:	 assignment
//	|	conditionalExpression
	;

assignment
	:	leftHandSide ASSIGN expression
;

leftHandSide
	:	 fieldAccess
//	|	expressionName
//	|	arrayAccess
	;

fieldAccess
	:	variableIdentifier
//	:	primary '.' Identifier
;

////////////////////////////////////////////////////////////////////////////////////
////////////   FUNCTION INVOCATION

functionInvocation
	: functionName '(' argumentList? ')'
;

argumentList
	:	expression (',' expression)*
;

functionName
	:	expressionName
;

////////////////////////////////////////////////////////////////////////////////////
////////////   TYPES

unannType
	:	unannPrimitiveType
	|	unannRecordType
	;

unannPrimitiveType
	:	numericType
	|	stringType
	|	dateType
;

stringType
	:	variableLengthStringType
	|	fixedLengthStringType
;

variableLengthStringType
	:	STRING
;

fixedLengthStringType
	:	CHAR '(' fixedLengthDimension ')'
;

fixedLengthDimension
	:	NonZeroDigit Digit*
;

numericType
	:	integralType
	|	floatingPointType
;

integralType
	:	INTEGER
;

floatingPointType
	:	FLOAT
;

dateType
	:	DATE
;

unannRecordType
	:	RECORD expressionName
;

//////////////////////////////////////////////////////////////////////////////////////
///////////  EXPRESSION NAMES - FOR TYPES

expressionName
	:	Identifier
	|	ambiguousName '.' Identifier
;

ambiguousName
	:	{isModule()}? Identifier
	|	ambiguousName '.' Identifier
;


// STATEMENTS

statement
	:	statementWithoutTrailingSubstatement
//	|	labeledStatement
//	|	ifThenStatement
//	|	ifThenElseStatement
//	|	whileStatement
//	|	forStatement
;

statementWithoutTrailingSubstatement
	:	expressionStatement
	|	emptyStatement
//	|	block
//	|	assertStatement
//	|	switchStatement
//	|	doStatement
//	|	breakStatement
//	|	continueStatement
//	|	returnStatement
//	|	synchronizedStatement
//	|	throwStatement
//	|	tryStatement
;

emptyStatement
	:	';'
;

expressionStatement
	:	statementExpression ';'
;

statementExpression
	:	callInternalProcedure
//	:	assignment
//	|	preIncrementExpression
//	|	preDecrementExpression
//	|	postIncrementExpression
//	|	postDecrementExpression
//	|	methodInvocation
//	|	classInstanceCreationExpression
;


////////////////////////////////////////////////////////////////////////////////////
///////////       BUILTIN FUNCTIONS & STATEMENTS

// STATEMENTS

callInternalProcedure
	:	printStatement
	|	openFileStatement
	|	closeFileStatement
	|	readStatement
;

printStatement
	:	PRINT concatStrings+ ';'
;

openFileStatement
	:	OPEN FILE  ( INPUT | OUTPUT ) stringLiteral ';'
;

closeFileStatement
	:	CLOSE ( INPUT | OUTPUT ) ';'
;

readStatement
	:	READ variableIdentifier ( COUNT | UNTIL ) primary ';'
;

// FUNCTIONS

builtinFunctionCall
	:	builtinFunction
;

builtinFunction
	:	CURRENTDATE '(' ')'
;

concatStrings
	:	primary ( '&' primary )?
;

// PRODUCTIONS

primary
	:	literal
	|	variableIdentifier arrs?
;

arrs
	:	arr (arr)*
;

arr
	:	'[' literal ']'
;

// LITERALS


literal
	:	integerLiteral
	|	floatingPointLiteral
	|	stringLiteral
;

integerLiteral
	: 	DecimalIntegerLiteral
;

DecimalIntegerLiteral
	:	DecimalNumeral
;

DecimalNumeral
	:	'0'
	|	NonZeroDigit Digit*
;

Digit
	:	'0'
	|	NonZeroDigit
;

NonZeroDigit
	:	[1-9]
;

floatingPointLiteral
	:	DecimalFloatingPointLiteral
;

DecimalFloatingPointLiteral
	:	Digit+ '.' Digit? ExponentPart?
	|	'.' Digit+ ExponentPart?
	|	Digit+ ExponentPart
	|	Digit+
	;

ExponentPart
	:	ExponentIndicator SignedInteger
	;

ExponentIndicator
	:	[eE]
;

SignedInteger
	:	Sign? Digit+
;

Sign
	:	[+-]
;

stringLiteral
	:	'"' StringCharacters? '"'
;

StringCharacters
	:	StringCharacter+
;

StringCharacter
	:	[^"\\]
	|	EscapeSequence
;

EscapeSequence
	:	'\\' [btnfr"'\\]
;

///////////////////////////////////////////////////////////////////////////////
//////////     LANGUAGE KEYS

INCLUDE : [Ii] [Nn] [Cc] [Ll] [Uu] [Dd] [Ee];
ONCE : [Oo] [Nn] [Cc] [Ee];

DECLARE : [Dd] [Ee] [Cc] [Ll] [Aa] [Rr] [Ee];
MODULE : [Mm] [Oo] [Dd] [Uu] [Ll] [Ee];
INTERFACE : [Ii] [Nn] [Tt] [Ee] [Rr] [Ff] [Aa] [Cc] [Ee];

VARIABLE : [Vv] [Aa] [Rr] [Ii] [Aa] [Bb] [Ll] [Ee];
FUNCTION : [Ff] [Uu] [Nn] [Cc] [Tt] [Ii] [Oo] [Nn];
STATEMENT : [Ss] [Tt] [Aa] [Tt] [Ee] [Mm] [Ee] [Nn] [Tt];
IN : [Ii] [Nn];
OUT : [Oo] [Uu] [Tt];
DROP : [Dd] [Rr] [Oo] [Pp];
FIELD : [Ff] [Ii] [Ee] [Ll] [Dd];
RECORD : [Rr] [Ee] [Cc] [Oo] [Rr] [Dd];
IS : [Ii] [Ss];

PUBLIC : [Pp] [Uu] [Bb] [Ll] [Ii] [Cc];
NATIVE : [Nn] [Aa] [Tt] [Ii] [Vv] [Ee];

PRIMITIVE : [Pp] [Rr] [Ii] [Mm] [ii] [Tt] [Ii] [Vv] [Ee];
INTEGER : [Ii] [Nn] [Tt] [Ee] [Gg] [Ee] [Rr];
FLOAT : [Ff] [Ll] [Oo] [Aa] [Tt];
STRING : [Ss] [Tt] [Rr] [Ii] [Nn] [Gg];
CHAR : [Cc] [Hh] [Aa] [Rr];
DATE : [Dd] [Aa] [Tt] [Ee];

CONSTANT : [Cc] [Oo] [Nn] [Ss] [Tt] [Aa] [Nn] [Tt];

RETURN : [Rr] [Ee] [Tt] [Uu] [Rr] [Nn];

///////////////////////////////////////////////////////////////////////////////
//////////     BUILTIN FUNCTION NAMES

CURRENTDATE : [Cc] [Uu] [Rr] [Rr] [Ee] [Nn] [Tt] [Dd] [Aa] [Tt] [Ee];

///////////////////////////////////////////////////////////////////////////////
//////////     BUILTIN STATEMENT NAMES

PRINT : [Pp] [Rr] [Ii] [Nn] [Tt];

OPEN : [Oo] [Pp] [Ee] [Nn];
FILE : [Ff] [Ii] [Ll] [Ee];
INPUT : [Ii] [Nn] [Pp] [Uu] [Tt];
OUTPUT : [Oo] [Uu] [Tt] [Pp] [Uu] [Tt];

CLOSE : [Cc] [Ll] [Oo] [Ss] [Ee];

READ : [Rr] [Ee] [Aa] [Dd];
COUNT : [Cc] [Oo] [Uu] [Nn] [Tt];
UNTIL : [Uu] [Nn] [Tt] [Ii] [Ll];

// separators

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI : ';';
COMMA : ',';
DOT : '.';

// operators

ASSIGN : '=';
GT : '>';
LT : '<';
BANG : '!';
TILDE : '~';
QUESTION : '?';
//COLON : ':';
EQUAL : '==';
LE : '<=';
GE : '>=';
NOTEQUAL : '!=';
AND : '&&';
OR : '||';
INC : '++';
DEC : '--';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
BITAND : '&';
BITOR : '|';
CARET : '^';
MOD : '%';
ARROW : '->';
//COLONCOLON : '::';

// identifiers

DOLLAR : '$';

recordIdentifier
	:	LetterOrDigit+
;

variableIdentifier
	:	DOLLAR LetterOrDigit+
;

moduleIdentifier
	:	LetterOrDigit+
;

Identifier
	:	Letter LetterOrDigit*
;

Letter
	:	[a-zA-Z_] // these are the "java letters" below 0x7F
;

LetterOrDigit
	:	[a-zA-Z0-9_] // these are the "java letters or digits" below 0x7F
;

//
// Whitespace and comments
//

WS  : [ \t\r\n\u000C]+  -> skip
;

COMMENT
    :   '/*' .*? '*/' -> skip
;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
;



