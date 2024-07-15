<<template>
    <div id="content">
        <table>
            <tr>
                <td>
                    <table style="border: 1px solid gray; width: 380px; font-size: 90%;">
                        <tr align="left">
                            <td>逗号位置：</td>
                            <td><input type="radio" name="kanma" value="0">前</td>
                            <td><input type="radio" name="kanma" value="1" checked>后</td>
                        </tr>
                        <tr align="left">
                            <td>AND/OR/ON位置：</td>
                            <td><input type="radio" name="andor" value="0" checked>前</td>
                            <td><input type="radio" name="andor" value="1">后</td>
                        </tr>
                        <tr align="left">
                            <td>缩进：</td>
                            <td><input type="radio" name="indent" value="0">tab</td>
                            <td><input type="radio" name="indent" value="1" checked>space4</td>
                        </tr>
                        <tr align="left">
                            <td>JOIN形式：</td>
                            <td><input type="radio" name="join" value="0" checked>前</td>
                            <td><input type="radio" name="join" value="1">后</td>
                        </tr>
                    </table>
                </td>
                <td>
                    <table style="border: 1px solid gray; width: 380px; font-size: 90%; height:100px;">
                        <tr align="left" rowspan="1" vAlign="top">
                            <td>sql start with：</td>
                            <td><input type="text" name="sqlPre" id="sqlPre" size="10" value="Preparing: " readonly></td>
                        </tr>
                        <tr align="left" vAlign="top">
                            <td>params start with：</td>
                            <td><input type="text" name="paramPre" id="paramPre" size="10" value="Parameters: " readonly></td>
                        </tr>
                    </table>
                </td>
            </tr>
            </table>
    </div>
</template>

<script type="text/javascript">
	eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('9 5=5||{};5.7=5.7||{};5.k=5.k||{};5.7.O="\\t";5.7.1x={".":3,"(":3,",":p};5.7.1y={".":3,",":3,";":3,")":3,"(*)":3,"(+)":3};5.7.1z={"1e":p,"P":3,"Q":3,"2f":3,"R":3,"Y":3,"Z":3,"1A":3,"10":3,"H":p,"D":3,",":3,"(":3,";":3,"I":3,"E":p,"J":p,"2g":3,"1f":3,"1g":3,"11":3,"D I":3,"q s w":3,"q C s w":3};5.7.1B={"12":3,"Q":3,"R":3,"Y":3,"Z":3,"E":3,"J":3,"1C":3,"1D":3,"1E":3,"10":3,"D":3,")":3,";":3,"q":3,"2h":3,"1F":3,"P":3,",":p,"H":3,"1e":p,"1G":3,"1H":3,"1I":3,"1J":3,"1K":3,"1f":3,"1g":3,"11":3,"D I":3,"q s w":3,"q C s w":3};5.7.1h={"2i":"E"};5.7.1L={"1e":p,"P":3,"Q":3,"12":3,"R":3,"Y":3,"Z":3,"E":3,"J":3,"1A":3,"10":3,"(":3,"2j":3,"H":3,"11":3,"s":3,"2k":3,"q s w":3,"q C s w":3};5.7.1M={"Y":3,"E":3,"J":3,"10":3,"D":3,"1F":3,"H":3,"1f":3,"1g":3,"11":3,"D I":3,"q s w":3,"q C s w":3};5.7.1N={"P":3,"12":3,"Q":3,"R":3,"E":3,"J":3,"H":3};5.7.1i={"1G":3,"1H":3,"1I":3,"1J":3,"1K":3};5.7.1O={"P":3};5.7.1P={"R":3,"Z":3,"1D":3,"1C":3,"1E":3,"12":3,"Q":3};5.7.1Q={"(":")","q s w":"q C s w"};5.7.1R={"D":"I","D I":3,"q":{"s":3,"C":3},"q s":"w","q s w":3,"q C":"s","q C s":"w","q C s w":3};5.7.1j={"E":5.7.O,"J":5.7.O,"H":5.7.O};5.13=8(a){4.F=v K();4.14=0;4.S=m;6(a){4.x(a)}};5.13.u={x:8(a){4.F.15(a);4.14+=a.o;4.S=m},L:8(){6(!4.S){4.S=4.F.1S("")}j 4.S},o:8(){j 4.14},T:8(i){j 4.L().2l(i)},2m:8(){4.14=0;4.F.o=0}};5.k.M=8(){};5.k.M.u={1T:0,1U:1,2n:2,U:8(a){6(!4.16){4.16=v K()}4.16.15(a)},1V:8(){j 4.16},1k:8(){j m},1W:8(a){4.1X=a},1Y:8(){j 4.1X}};5.k.A=8(a){4.1Z=a;4.1l=v K();4.17=5.k.A.u.17++};5.k.A.u=v 5.k.M();5.k.A.u.17=0;5.k.A.u.1k=8(){j 5.k.M.1U};5.k.A.u.1m=8(a){j 4.1l[a]};5.k.A.u.1n=8(a,b){4.1l[a]=b};5.k.A.u.20=8(){j 4.1Z};5.k.A.u.21=8(){j 4.17};5.k.N=8(){};5.k.N.u=v 5.k.M();5.k.N.u.1k=8(){j 5.k.M.1T};5.k.N.u.18=8(a){j v 5.k.A(a)};5.k.N.u.2o=8(a){j v 5.k.2p(a)};5.7.22=8(a){6(a!=m){4.1o=v 5.7.19(a);4.7=a}4.1a=v K();4.1b=v K();4.V=m;4.23="";4.z=0;4.2q={};4.1c={};4.1p={}};5.7.22.u={2r:8(a){6(a!=m){4.1o=v 5.7.19(a);4.7=a}6(4.7==m||4.7.o==0){j""}9 b=v 5.k.N();9 c=b.18("2s");c.1n("1d",3);4.1q(b,c);9 d=v 5.13();4.1r(d,c);4.24(d);j d.L()},24:8(a){6(a.o<=0){j}9 b=a.F.25();6(b){a.x(b.26(/([ \\t]+$)/g,\'\'))}},2t:8(a){9 b=a.F[a.F.o-1];6(b==m||b.T(b.o-1)=="\\n"){j 3}j p},27:8(a,b,c,d,e){9 f=c.1s();9 g=p;9 h="";9 i=5.7.1Q[f];6(i){4.1a.15(4.z);4.1b[e+":"+i]=3}l 6(4.1b[e+":"+f]){4.z=4.1a.25();2u 4.1b[f]}l 6(f==";"){4.z=0;4.1a.o=0}6(5.7.1y[f]){b=b.26(/([ \\t]+$)/g,\'\')}6(5.7.1M[f]){6(4.z>0){4.z--}}6(5.7.1i[f]){6(4.1p[e]==m){4.1p[e]=3}l{4.z--}}6(5.7.1O[f]){4.1c[e]=4.z}6(5.7.1P[f]&&4.1c[e]!=m){4.z=4.1c[e]}6(5.7.1h[f]){4.V=5.7.1h[f]}6(5.7.1B[f]){6(d&&4.V!=f){b="\\r\\n"}}6(4.V==f){h=" ";4.V=m}l 6((5.7.1z[f]&&d)||f.28(/^\\-\\-/)||f.28(/\\*\\/$/)){h="\\r\\n"}l 6(5.7.1j[f]){h=5.7.1j[f]}l 6(5.7.1x[f]){h=""}l{h=" "}6(b!=m&&b.T(b.o-1)==\'\\n\'){6(a.o()>0){a.x(b+4.1t(4.z)+c)}l{a.x(4.1t(4.z)+c)}}l{a.x((b==m?"":b)+c)}6(5.7.1L[f]||5.7.1i[f]){4.z++}4.23=f;j h},1r:8(a,b){9 c=b.1V();6(c==m){j}1u(9 i=0;i<c.o;i++){6(c[i].20()=="29"){4.1r(a,c[i]);G}9 d=b.1m("1d");9 e=b.21();4.2a=4.27(a,4.2a,c[i].1Y(),d,e)}j},1q:8(a,b){9 c;9 d=b.1m("1d")||p;2b((c=4.1o.2c(5.7.1R))!=m){9 e=a.18("2v");e.1W(c);6(!d&&5.7.1N[c.1s()]){d=3;b.1n("1d",3)}6(c=="("){9 f=a.18("29");f.U(e);b.U(f);4.1q(a,f);G}l 6(c==")"){b.U(e);j}b.U(e)}},1t:8(a){9 b="";1u(9 i=0;i<a;i++){b+=5.7.O}j b}};5.7.19=8(a){4.W={",":3,"(":3,")":3,"=":3,"<":3,">":3,"+":3,"-":3,"*":3,"/":3,".":3,";":3,"|":3};4.X={" ":3,"\\t":3,"\\r":3,"\\n":3};4.1v={"<":{"=":3,">":3},"(":{"*":3,"+":3},">":"=","!":"=","(+":")","(*":")","/":"*","*":"/","-":"-","|":"|","<=":3,">=":3,"!=":3,"<>":3,"(+)":3,"(*)":3,"/*":3,"*/":3,"--":3,"||":3};4.2w=m;4.7=a;4.1w=0};5.7.19.u={2c:8(a){9 b;9 c;6(4.B&&4.B.o>0){j 4.B.2d()}6(!4.B){4.B=v K()}2b((b=4.2e())!=m){4.B.15(b);c=4.B.1S(" ");9 d=a[c.1s()];6(d==m){j 4.B.2d()}l 6(d==3){y}l{G}y}6(4.B.o>0){4.B.o=0;j c}l{j m}},2e:8(){9 a;9 b;9 c=v 5.13();9 d=p;9 e=p;9 f=p;9 g=p;6(4.7==m){j m}1u(9 i=4.1w;i<4.7.o;i++){a=4.7.T(i);6(i+1<4.7.o){b=4.7.T(i+1)}l{b=""}6(!e&&!f&&!g&&a=="\\\'"){d=!d;6(!d){c.x(a);y}}l 6(!d&&!f&&!g&&a=="\\""){e=!e;6(!e){c.x(a);y}}l 6(!d&&!e&&!g&&a=="-"&&b=="-"){6(!f&&c.o()>0){i--;y}f=3}l 6(f&&(a=="\\r"||a=="\\n")){f=p;y}l 6(!d&&!e&&!f&&a=="/"&&b=="*"){6(c.o()>0){i--;y}g=3}l 6(g&&a=="*"&&b=="/"){g=p;c.x(a+b);i++;y}6(!d&&!e&&!f&&!g){6(4.W[a]){6(c.o()==0){c.x(a);6(!4.W[b]&&!4.X[b]){y}G}9 h=4.1v[c.L()];6(h){6(h==a||h[a]){c.x(a);6(4.1v[c.L()]==3){y}G}}i--;y}l 6(4.X[a]){6(4.W[b]||4.X[b]){G}6(c.o()>0){y}l{G}}l{6(4.W[b]||4.X[b]){c.x(a);y}}}c.x(a)}4.1w=i+1;6(c.o()>0){j c.L()}l{j m}}};',62,157,'|||true|this|atl|if|sql|function|var||||||||||return|dom|else|null||length|false|WHEN||MATCHED||prototype|new|THEN|append|break|indentCnt|Element|joinArray|NOT|UNION|AND|array|continue|ON|ALL|OR|Array|toString|Node|Document|INDENT_STRING|SELECT|UPDATE|FROM|stringCache|charAt|appendChild|notBreakToken|TOKEN_END_KIGOH_MAP|TOKEN_END_MAP|SET|WHERE|VALUES|USING|INSERT|StringBuffer|len|push|childArray|elementId|createElement|SqlTokenizer|machingIndentArray|machingEndKeyMap|baseIndentMap|BREAK|WITH|MINUS|INTERSECT|NOT_BREAK_TOKEN_MAP|BLOCK_INDENT_NEXT_MINUS_ALL_PLUS_TOKEN_MAP|SPECIAL_LAST_JOIN_TOKEN_MAP|getNodeType|attributeHash|getAttribute|setAttribute|tokenizer|baseIndentPlusMinusFirstFlgMap|createSqlNode|makeSqlBuffer|toUpperCase|getIndent|for|TOKEN_RENKETSU_KIGOH_MAP|tokenIdx|ATO_NO_SPACE_TOHKEN_KIGOH_MAP|MAE_NO_SPACE_TOHKEN_KIGOH_MAP|ATO_BREAK_TOKEN_MAP|BY|MAE_BREAK_TOKEN_MAP|GROUP|HAVING|ORDER|END|LEFT|RIGHT|INNER|FULL|NATURAL|INDENT_PLUS_TOKEN_MAP|INDENT_MINUS_TOKEN_MAP|KAKKO_BREAK_TOKEN_MAP|BASE_INDENT_SEARCH_MAP|BASE_BLOCK_LEVEL_INDENT_MAP|MACHING_INDENT_MAP|SPECIAL_JOIN_TOKEN_MAP|join|DOCUMENT_NODE|ELEMENT_NODE|getChildNodes|setNodeValue|value|getNodeValue|tagName|getTagName|getElementId|SqlFormatter|oldUpperToken|rTrimBuffer|pop|replace|formatToken|match|block|staticSpliter|while|nextTokenJoin|shift|nextToken|INTO|JOIN|ELSE|BETWEEN|CASE|MERGE|substring|clear|TEXT_NODE|createTextNode|Text|machingIndentMap|format|root|isLastBreakBuffer|delete|token|specialJoinMap'.split('|'),0,{})) //"
    
    export default {
        name: "sqlFormat",
        data() {
            return {
            
            };
        },
        activated() {
            this.onload();
        },
        methods: {
            onload(){
                var object = {
                    first :{
                        type:"label",
                        id :"title",
                        name:"title",
                        text :"GETTING SQL FROM LOG",
                        position:"center",
                        size:"20pt",
                        width:"100%",
                        color :"blue"
                    },
                    second :{
                        type:"textarea",
                        id :"firstTextArea",
                        name:"firstTextArea",
                        row:"10",
                        col:"180"
                    },
                    buttonFirst :{
                        type:"button",
                        id :"firstButton",
                        name:"firstButton",
                        text:"showSql",
                        onclick:this.showSQL
                    },
                    third :{
                        type:"label",
                        id :"result",
                        name:"result",
                        size:"30pt",
                        width:"100%",
                        color :"red"
                    },
                    resultTextArea :{
                        type:"textarea",
                        id :"resultTextArea",
                        name:"resultTextArea",
                        row:"20",
                        col:"180"
                    }
                };
                this.doCreate(object);
            },
            doCreate(object){
                for(var control in object){
                    var myObject = object[control]
                    var controller = document.createElement(myObject.type);

                    controller.id = myObject.id;
                    // controller.localName = myObject.name;

                    if(myObject.type =="textarea"){
                            controller.rows = myObject.row;
                            controller.cols = myObject.col;
                    }
                    
                    if(myObject.text!=undefined){
                        if(myObject.text!=""){
                            controller.innerHTML=myObject.text;
                        }
                    }
                    if(myObject.position!=undefined){
                        controller.style.textAlign =myObject.position;
                    }
                    if(myObject.size!=undefined){
                        controller.style.fontSize = myObject.size;
                    }
                    if(myObject.width!=undefined){
                        controller.style.width = myObject.width;
                    }
                    if(myObject.color!=undefined){
                        controller.style.color = myObject.color;
                    }

                    if(myObject.onclick!=undefined){
                        controller.onclick = myObject.onclick;
                    }
                    document.getElementById("content").appendChild(controller);
                    if(control =="buttonFirst"){
                        document.getElementById("content").appendChild(document.createElement("hr"));
                    }
                }
            },
            getVersion(){
                var returnValue;
                var ie=navigator.userAgent.toLowerCase().indexOf("msie");
                var firefox=navigator.userAgent.toLowerCase().indexOf("firefox");
                if(ie>=0 && firefox<0){
                    returnValue = "1"
                }
                if(ie<0 && firefox>=0){
                    returnValue = "2"
                }
                return returnValue;
            },
            showSQL(){
                var sqlPre = document.getElementById("sqlPre").value;
                var paramPre = document.getElementById("paramPre").value;
                var strContnt = document.getElementById("firstTextArea").value;
                var strArray = strContnt.split('\n');
                if(strArray == null || strArray.length < 2){
                    alert("please input correct sql!");
                    return;
                }
                var sqlStrLine = strArray[0];
                var paramStrLine = strArray[1];
                var sqlStart = sqlStrLine.indexOf(sqlPre);
                var sqlStr = sqlStrLine.substr(sqlStart+sqlPre.length);
                sqlStr = sqlStr.replace(/,/g,"#");

                var paramStart = paramStrLine.indexOf(paramPre);
                var paramStr = paramStrLine.substr(paramStart+paramPre.length).replace("[","").replace("]","").replace(/\(/g,"").replace(/\)/g,"");
                paramStr = paramStr.replaceAll("String","").replaceAll("Long","").replaceAll("BigDecimal","").replaceAll("Date","");
                paramStr = paramStr.replace(/,\ /g,",");

                var sqlArray = [];
                var array = [];
                var paramArray = [];
                var count =0;
                var bcount = 0;

                for(var i=0; i<sqlStr.length; i++){
                    sqlArray.push(sqlStr.substring(count,count+1));
                    count = count+1;
                }

                for(var i=0; i<sqlArray.length; i++){
                    if(sqlArray[i]=="?"){
                        array.push(bcount);
                    }
                    bcount = bcount+1;
                }

                var temp = "";
                paramArray = paramStr.split(",");
                if(array.length != paramArray.length){
                    alert("Please check the number of params!");
                }
                for(var i=0; i<array.length; i++){
                    temp = paramArray[i];
                    if(temp =="null"){
                        sqlArray[array[i]] = temp;
                    }else{
                        sqlArray[array[i]] = "'"+temp+"'";
                    }
                }
            
                var outString = sqlArray.toString().replace(/,/g,"").replace(/#/g,",");

                document.getElementById("resultTextArea").value = outString;
            
                window.clipboardData.setData('text',outString.replace(/&gt;/g,">").replace(/&lt;/g,"<"));
            
                format();
            },
            format() {
				var element = document.getElementById('resultTextArea');
				var kanmaElements = document.getElementsByName("kanma");
				var andorElements = document.getElementsByName("andor");
				var indentElements = document.getElementsByName("indent");
				var joinElements = document.getElementsByName("join");

				if (kanmaElements[0].checked) {
					atl.sql.ATO_BREAK_TOKEN_MAP[","] = false;
					atl.sql.MAE_BREAK_TOKEN_MAP[","] = true;
					atl.sql.MAE_NO_SPACE_TOHKEN_KIGOH_MAP[","] = false;
					atl.sql.ATO_NO_SPACE_TOHKEN_KIGOH_MAP[","] = true;
				} else {
					atl.sql.ATO_BREAK_TOKEN_MAP[","] = true;
					atl.sql.MAE_BREAK_TOKEN_MAP[","] = false;
					atl.sql.MAE_NO_SPACE_TOHKEN_KIGOH_MAP[","] = true;
					atl.sql.ATO_NO_SPACE_TOHKEN_KIGOH_MAP[","] = false;
				}
				if (andorElements[0].checked) {
					atl.sql.ATO_BREAK_TOKEN_MAP["AND"] = false;
					atl.sql.MAE_BREAK_TOKEN_MAP["AND"] = true;
					atl.sql.ATO_BREAK_TOKEN_MAP["OR"] = false;
					atl.sql.MAE_BREAK_TOKEN_MAP["OR"] = true;
					atl.sql.ATO_BREAK_TOKEN_MAP["ON"] = false;
					atl.sql.MAE_BREAK_TOKEN_MAP["ON"] = true;
				} else {
					atl.sql.ATO_BREAK_TOKEN_MAP["AND"] = true;
					atl.sql.MAE_BREAK_TOKEN_MAP["AND"] = false;
					atl.sql.ATO_BREAK_TOKEN_MAP["OR"] = true;
					atl.sql.MAE_BREAK_TOKEN_MAP["OR"] = false;
					atl.sql.ATO_BREAK_TOKEN_MAP["ON"] = true;
					atl.sql.MAE_BREAK_TOKEN_MAP["ON"] = false;
				}
				if (indentElements[0].checked) {
					atl.sql.INDENT_STRING = "\t";
					atl.sql.SPECIAL_LAST_JOIN_TOKEN_MAP["AND"] = atl.sql.INDENT_STRING;
					atl.sql.SPECIAL_LAST_JOIN_TOKEN_MAP["OR"]  = atl.sql.INDENT_STRING;
					atl.sql.SPECIAL_LAST_JOIN_TOKEN_MAP["ON"]  = atl.sql.INDENT_STRING;
				} else {
					atl.sql.INDENT_STRING = "    ";
					atl.sql.SPECIAL_LAST_JOIN_TOKEN_MAP["AND"] = " ";
					atl.sql.SPECIAL_LAST_JOIN_TOKEN_MAP["OR"] = "  ";
					atl.sql.SPECIAL_LAST_JOIN_TOKEN_MAP["ON"] = "  ";
				}

				if (joinElements[0].checked) {
					atl.sql.ATO_BREAK_TOKEN_MAP["JOIN"]  = true;
				} else if (joinElements[1].checked) {
					atl.sql.ATO_BREAK_TOKEN_MAP["JOIN"]  = false;
				}

				if (element.value.length > 0) {
					var formater = new atl.sql.SqlFormatter(element.value);
					element.value = formater.format();
				}
			}
        }
    };

    String.prototype.replaceAll = function(s1,s2) { 
				return this.replace(new RegExp(s1,"gm"),s2); 
			}
    
</script>