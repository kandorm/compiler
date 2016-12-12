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
    _T5 = 0
    _T3 = _T5
    branch _L10
_L11:
    _T6 = 1
    _T7 = (_T3 + _T6)
    _T3 = _T7
_L10:
    _T8 = 10
    _T9 = (_T3 <= _T8)
    if (_T9 == 0) branch _L12
    branch _L11
_L12:
}

