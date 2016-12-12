VTABLE(_TreeNode) {
    <empty>
    TreeNode
    _TreeNode.init;
    _TreeNode.print;
    _TreeNode.getleft;
    _TreeNode.getright;
}

VTABLE(_Main) {
    <empty>
    Main
}

FUNCTION(_TreeNode_New) {
memo ''
_TreeNode_New:
    _T11 = 16
    parm _T11
    _T12 =  call _Alloc
    _T13 = 0
    *(_T12 + 4) = _T13
    *(_T12 + 8) = _T13
    *(_T12 + 12) = _T13
    _T14 = VTBL <_TreeNode>
    *(_T12 + 0) = _T14
    return _T12
}

FUNCTION(_Main_New) {
memo ''
_Main_New:
    _T15 = 4
    parm _T15
    _T16 =  call _Alloc
    _T17 = VTBL <_Main>
    *(_T16 + 0) = _T17
    return _T16
}

FUNCTION(_TreeNode.init) {
memo '_T0:4 _T1:8 _T2:12 _T3:16'
_TreeNode.init:
    _T18 = *(_T0 + 4)
    *(_T0 + 4) = _T1
    _T19 = *(_T0 + 8)
    *(_T0 + 8) = _T2
    _T20 = *(_T0 + 12)
    *(_T0 + 12) = _T3
}

FUNCTION(_TreeNode.print) {
memo '_T4:4'
_TreeNode.print:
    _T21 = *(_T4 + 12)
    parm _T21
    call _PrintString
    _T22 = "\n"
    parm _T22
    call _PrintString
}

FUNCTION(_TreeNode.getleft) {
memo '_T5:4'
_TreeNode.getleft:
    _T23 = *(_T5 + 4)
    return _T23
}

FUNCTION(_TreeNode.getright) {
memo '_T6:4'
_TreeNode.getright:
    _T24 = *(_T6 + 8)
    return _T24
}

FUNCTION(_Main.lowestCommonAncestor) {
memo '_T7:4 _T8:8 _T9:12 _T10:16'
_Main.lowestCommonAncestor:
    _T25 = (_T7 == _T10)
    if (_T25 == 0) branch _L16
    return _T10
_L16:
    _T26 = (_T7 == _T8)
    _T27 = (_T7 == _T9)
    _T28 = (_T26 || _T27)
    if (_T28 == 0) branch _L17
    return _T7
_L17:
    parm _T7
    _T31 = *(_T7 + 0)
    _T32 = *(_T31 + 16)
    _T33 =  call _T32
    parm _T33
    parm _T8
    parm _T9
    parm _T10
    _T34 =  call _Main.lowestCommonAncestor
    _T29 = _T34
    parm _T7
    _T35 = *(_T7 + 0)
    _T36 = *(_T35 + 20)
    _T37 =  call _T36
    parm _T37
    parm _T8
    parm _T9
    parm _T10
    _T38 =  call _Main.lowestCommonAncestor
    _T30 = _T38
    _T39 = (_T29 == _T10)
    _T40 = (_T30 == _T10)
    _T41 = (_T39 && _T40)
    if (_T41 == 0) branch _L18
    _T42 = 0
    return _T42
_L18:
    _T43 = (_T29 != _T10)
    _T44 = (_T30 != _T10)
    _T45 = (_T43 && _T44)
    if (_T45 == 0) branch _L19
    return _T7
_L19:
    return <empty>
}

FUNCTION(main) {
memo ''
main:
    _T56 =  call _TreeNode_New
    _T54 = _T56
    _T57 =  call _TreeNode_New
    _T46 = _T57
    _T58 =  call _TreeNode_New
    _T47 = _T58
    _T59 =  call _TreeNode_New
    _T48 = _T59
    _T60 =  call _TreeNode_New
    _T49 = _T60
    _T61 =  call _TreeNode_New
    _T50 = _T61
    _T62 =  call _TreeNode_New
    _T51 = _T62
    _T63 =  call _TreeNode_New
    _T52 = _T63
    _T64 =  call _TreeNode_New
    _T53 = _T64
    _T65 = "A"
    parm _T46
    parm _T47
    parm _T48
    parm _T65
    _T66 = *(_T46 + 0)
    _T67 = *(_T66 + 8)
    call _T67
    _T68 = "B"
    parm _T47
    parm _T50
    parm _T49
    parm _T68
    _T69 = *(_T47 + 0)
    _T70 = *(_T69 + 8)
    call _T70
    _T71 = "C"
    parm _T48
    parm _T51
    parm _T52
    parm _T71
    _T72 = *(_T48 + 0)
    _T73 = *(_T72 + 8)
    call _T73
    _T74 = "D"
    parm _T49
    parm _T53
    parm _T54
    parm _T74
    _T75 = *(_T49 + 0)
    _T76 = *(_T75 + 8)
    call _T76
    _T77 = "E"
    parm _T50
    parm _T54
    parm _T54
    parm _T77
    _T78 = *(_T50 + 0)
    _T79 = *(_T78 + 8)
    call _T79
    _T80 = "F"
    parm _T51
    parm _T54
    parm _T54
    parm _T80
    _T81 = *(_T51 + 0)
    _T82 = *(_T81 + 8)
    call _T82
    _T83 = "G"
    parm _T52
    parm _T54
    parm _T54
    parm _T83
    _T84 = *(_T52 + 0)
    _T85 = *(_T84 + 8)
    call _T85
    _T86 = "H"
    parm _T53
    parm _T54
    parm _T54
    parm _T86
    _T87 = *(_T53 + 0)
    _T88 = *(_T87 + 8)
    call _T88
    parm _T46
    parm _T47
    parm _T49
    parm _T54
    _T89 =  call _Main.lowestCommonAncestor
    _T55 = _T89
    parm _T55
    _T90 = *(_T55 + 0)
    _T91 = *(_T90 + 12)
    call _T91
    parm _T46
    parm _T48
    parm _T49
    parm _T54
    _T92 =  call _Main.lowestCommonAncestor
    _T55 = _T92
    parm _T55
    _T93 = *(_T55 + 0)
    _T94 = *(_T93 + 12)
    call _T94
    parm _T46
    parm _T52
    parm _T51
    parm _T54
    _T95 =  call _Main.lowestCommonAncestor
    _T55 = _T95
    parm _T55
    _T96 = *(_T55 + 0)
    _T97 = *(_T96 + 12)
    call _T97
    parm _T46
    parm _T53
    parm _T50
    parm _T54
    _T98 =  call _Main.lowestCommonAncestor
    _T55 = _T98
    parm _T55
    _T99 = *(_T55 + 0)
    _T100 = *(_T99 + 12)
    call _T100
    parm _T46
    parm _T53
    parm _T52
    parm _T54
    _T101 =  call _Main.lowestCommonAncestor
    _T55 = _T101
    parm _T55
    _T102 = *(_T55 + 0)
    _T103 = *(_T102 + 12)
    call _T103
}

