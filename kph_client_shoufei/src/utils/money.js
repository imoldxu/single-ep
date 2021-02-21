export const regFenToYuan = (fen) =>{
    var num = fen;
    num=fen*0.01;
    num+='';
    var reg = num.indexOf('.') >-1 ? /(\d{1,3})(?=(?:\d{3})+\.)/g : /(\d{1,3})(?=(?:\d{3})+$)/g;
    num=num.replace(reg,'$1');
    num = toDecimal2(num)
    return num
};

export const regYuanToFen = (yuan,digit) =>{
    var m=0,
        s1=yuan.toString(),
        s2=digit.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}

export const checkTwoPointNum = (inputNumber) => {
    var partten = /^-?\d+\.?\d{0,2}$/;
    return partten.test(inputNumber)
}

export const toDecimal2 = (x) => {
    var f = parseFloat(x);
    if (isNaN(f)) {
      return false;
    }
    var f = Math.round(x * 100) / 100;
    var s = f.toString();
    var rs = s.indexOf('.');
    if (rs < 0) {
      rs = s.length;
      s += '.';
    }
    while (s.length <= rs + 2) {
      s += '0';
    }
    return s;
}