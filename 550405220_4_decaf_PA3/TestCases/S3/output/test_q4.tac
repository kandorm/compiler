VTABLE(_Main) {
    <empty>
    Main
}

FUNCTION(_Main_New) {
memo ''
_Main_New:
    _T0 = 4
    parm _T0
    _T1 =  call _Alloc
    _T2 = VTBL <_Main>
    *(_T1 + 0) = _T2
    return _T1
}

FUNCTION(main) {
memo ''
main:
    _T4 = 0
    _T3 = _T4
    parm _T3
    call _PrintInt
    _T5 = "\n\n"
    parm _T5
    call _PrintString
    _T6 = 0
    _T3 = _T6
    parm _T3
    call _PrintInt
    _T7 = "\n\n"
    parm _T7
    call _PrintString
    _T8 = 0
    _T3 = _T8
    parm _T3
    call _PrintInt
    _T9 = "\n\n"
    parm _T9
    call _PrintString
}

