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
_L10:
    _T5 = 10
    _T6 = (_T3 < _T5)
    if (_T6 == 0) branch _L11
    _T7 = 1
    _T8 = (_T3 + _T7)
    _T3 = _T8
    _T9 = 5
    _T10 = (_T3 > _T9)
    if (_T10 == 0) branch _L12
_L12:
    parm _T3
    call _PrintInt
    _T11 = "\n"
    parm _T11
    call _PrintString
    branch _L10
_L11:
    parm _T3
    call _PrintInt
    _T12 = "\n\n"
    parm _T12
    call _PrintString
    _T13 = 0
    _T3 = _T13
    _T14 = 0
    _T3 = _T14
    branch _L13
_L14:
    _T15 = 2
    _T16 = (_T3 + _T15)
    _T3 = _T16
_L13:
    _T17 = 34
    _T18 = (_T3 < _T17)
    if (_T18 == 0) branch _L15
    _T19 = 15
    _T20 = (_T3 > _T19)
    if (_T20 == 0) branch _L16
_L16:
    parm _T3
    call _PrintInt
    _T21 = "\n"
    parm _T21
    call _PrintString
    branch _L14
_L15:
    parm _T3
    call _PrintInt
    _T22 = "\n\n"
    parm _T22
    call _PrintString
    _T23 = 0
    _T3 = _T23
    parm _T3
    call _PrintInt
    _T24 = "\n\n"
    parm _T24
    call _PrintString
}

