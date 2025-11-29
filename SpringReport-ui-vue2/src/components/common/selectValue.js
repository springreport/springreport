/*
 * @Description: 下拉选项用共通js
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-04 08:36:18
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-08-18 08:48:42
 */
const selectUtil = {

}

selectUtil.datasourceType = [
  { value: 1, label: 'mysql' },
  { value: 2, label: 'oracle' },
  { value: 3, label: 'sqlServer' },
  { value: 4, label: 'api' },
  { value: 5, label: 'postgreSql' },
  { value: 6, label: 'influxdb' },
  { value: 7, label: '达梦' },
  { value: 8, label: 'clickhouse' },
  { value: 9, label: 'elasticsearch' },
  { value: 10, label: 'TDengine' },
  { value: 11, label: 'kingbase(人大金仓)' },
  { value: 12, label: '瀚高(highgo)' },
  { value: 13, label: 'doris' },
  { value: 14, label: 'mongodb' }
]
selectUtil.yesNo = [
  { value: 1, label: '是' },
  { value: 2, label: '否' }
]
selectUtil.menuAccessRule = [
  { value: 1, label: '登录后访问' },
  { value: 2, label: '登陆并授权后访问' }
]
selectUtil.apiAccessRule = [
  { value: 1, label: '公开访问' },
  { value: 2, label: '登录后访问' },
  { value: 3, label: '登录后并授权访问' }
]
selectUtil.textAlign = [
  { value: 'left', label: '左对齐' },
  { value: 'center', label: '居中对齐' },
  { value: 'right', label: '右对齐' }
]

selectUtil.verticalAlign = [
  { value: 'top', label: '顶端对齐' },
  { value: 'middle', label: '垂直居中' },
  { value: 'bottom', label: '底端对齐' }
]

selectUtil.fontWeight = [
  { value: 'normal', label: '正常' },
  { value: 'bold', label: '加粗' }
]
selectUtil.resultType = [
  { value: 'String', label: '字符串/数字' },
  { value: 'Array', label: '数组' },
  { value: 'ObjectArray', label: '对象数组' },
  { value: 'Object', label: '对象' }
]

selectUtil.apiResultType = [
  { value: 'ObjectArray', label: '对象数组' },
  { value: 'Object', label: '对象' }
]

selectUtil.printResultType = [
  { value: 'String', label: '字符串/数字' },
  { value: 'Object', label: '对象' }
]

selectUtil.chartTheme = [
  { value: 'dark', label: '默认' },
  { value: 'dark-blue', label: 'dark-blue' },
  { value: 'fresh-cu', label: 'fresh-cu' },
  { value: 'gray', label: 'gray' },
  { value: 'green', label: 'green' },
  { value: 'macarons2', label: 'macarons2' },
  { value: 'red', label: 'red' },
  { value: 'vintage', label: 'vintage' }
]

selectUtil.orient = [
  { value: 'horizontal', label: '水平' },
  { value: 'vertical', label: '垂直' }
]

selectUtil.labelPosition = [
  { value: 'outside', label: '饼图扇区外侧' },
  { value: 'inside', label: '饼图扇区内部' },
  { value: 'center', label: '饼图中心位置' }
]

selectUtil.roseType = [
  { value: '', label: '普通饼图' },
  { value: 'radius', label: '南丁格尔玫瑰图' }
]

selectUtil.dateFormat = [
  { value: '1', label: '年-月-日 时:分:秒' },
  { value: '2', label: '年-月-日 时:分' },
  { value: '3', label: '年-月-日' },
  { value: '4', label: '年-月' },
  { value: '5', label: '时:分:秒' },
  { value: '6', label: '时:分' },
  { value: '7', label: '星期几' },
  { value: '8', label: '年' },
  { value: '9', label: '月' },
  { value: '10', label: '日' }
]

selectUtil.textType = [
  { value: 'text', label: '普通文本' },
  { value: 'marquee', label: '滚动文本' },
  { value: 'link', label: '超链接' }
]

selectUtil.gaugeIconType = [
  { value: 'circle', label: 'circle' },
  { value: 'rect', label: 'rect' },
  { value: 'roundRect', label: 'roundRect' },
  { value: 'triangle', label: 'triangle' },
  { value: 'diamond', label: 'diamond' },
  { value: 'pin', label: 'pin' },
  { value: 'arrow', label: 'arrow' },
  { value: 'none', label: 'none' }
]

selectUtil.hyperLinkType = [
  { value: '1', label: '报表' },
  { value: '2', label: '大屏' },
  { value: '3', label: '外部链接' }
]

selectUtil.hyperLinkOpenType = [
  { value: '_blank', label: '新窗口' },
  { value: '_self', label: '当前窗口' }
]
selectUtil.columnDirection = [
  { value: 'vertical', label: '竖向' },
  { value: 'horizontal', label: '横向' }
]

selectUtil.map = [
  { value: '100000', label: '中华人民共和国' },
  { value: '110000', label: '北京市' },
  { value: '120000', label: '天津市' },
  { value: '130000', label: '河北省' },
  { value: '140000', label: '山西省' },
  { value: '150000', label: '内蒙古自治区' },
  { value: '210000', label: '辽宁省' },
  { value: '220000', label: '吉林省' },
  { value: '230000', label: '黑龙江省' },
  { value: '310000', label: '上海市' },
  { value: '320000', label: '江苏省' },
  { value: '330000', label: '浙江省' },
  { value: '340000', label: '安徽省' },
  { value: '350000', label: '福建省' },
  { value: '360000', label: '江西省' },
  { value: '370000', label: '山东省' },
  { value: '410000', label: '河南省' },
  { value: '420000', label: '湖北省' },
  { value: '430000', label: '湖南省' },
  { value: '440000', label: '广东省' },
  { value: '450000', label: '广西壮族自治区' },
  { value: '460000', label: '海南省' },
  { value: '500000', label: '重庆市' },
  { value: '510000', label: '四川省' },
  { value: '520000', label: '贵州省' },
  { value: '530000', label: '云南省' },
  { value: '540000', label: '西藏自治区' },
  { value: '610000', label: '陕西省' },
  { value: '620000', label: '甘肃省' },
  { value: '630000', label: '青海省' },
  { value: '640000', label: '宁夏回族自治区' },
  { value: '650000', label: '新疆维吾尔自治区' },
  { value: '710000', label: '台湾省' },
  { value: '810000', label: '香港特别行政区' },
  { value: '820000', label: '澳门特别行政区' },
  { value: '110100', label: '北京市市辖区' },
  { value: '120100', label: '天津市市辖区' },
  { value: '130100', label: '石家庄市' },
  { value: '130200', label: '唐山市' },
  { value: '130300', label: '秦皇岛市' },
  { value: '130400', label: '邯郸市' },
  { value: '130500', label: '邢台市' },
  { value: '130600', label: '保定市' },
  { value: '130700', label: '张家口市' },
  { value: '130800', label: '承德市' },
  { value: '130900', label: '沧州市' },
  { value: '131000', label: '廊坊市' },
  { value: '131100', label: '衡水市' },
  { value: '140100', label: '太原市' },
  { value: '140200', label: '大同市' },
  { value: '140300', label: '阳泉市' },
  { value: '140400', label: '长治市' },
  { value: '140500', label: '晋城市' },
  { value: '140600', label: '朔州市' },
  { value: '140700', label: '晋中市' },
  { value: '140800', label: '运城市' },
  { value: '140900', label: '忻州市' },
  { value: '141000', label: '临汾市' },
  { value: '141100', label: '吕梁市' },
  { value: '150100', label: '呼和浩特市' },
  { value: '150200', label: '包头市' },
  { value: '150300', label: '乌海市' },
  { value: '150400', label: '赤峰市' },
  { value: '150500', label: '通辽市' },
  { value: '150600', label: '鄂尔多斯市' },
  { value: '150700', label: '呼伦贝尔市' },
  { value: '150800', label: '巴彦淖尔市' },
  { value: '150900', label: '乌兰察布市' },
  { value: '152200', label: '兴安盟' },
  { value: '152500', label: '锡林郭勒盟' },
  { value: '152900', label: '阿拉善盟' },
  { value: '210100', label: '沈阳市' },
  { value: '210200', label: '大连市' },
  { value: '210300', label: '鞍山市' },
  { value: '210400', label: '抚顺市' },
  { value: '210500', label: '本溪市' },
  { value: '210600', label: '丹东市' },
  { value: '210700', label: '锦州市' },
  { value: '210800', label: '营口市' },
  { value: '210900', label: '阜新市' },
  { value: '211000', label: '辽阳市' },
  { value: '211100', label: '盘锦市' },
  { value: '211200', label: '铁岭市' },
  { value: '211300', label: '朝阳市' },
  { value: '211400', label: '葫芦岛市' },
  { value: '220100', label: '长春市' },
  { value: '220200', label: '吉林市' },
  { value: '220300', label: '四平市' },
  { value: '220400', label: '辽源市' },
  { value: '220500', label: '通化市' },
  { value: '220600', label: '白山市' },
  { value: '220700', label: '松原市' },
  { value: '220800', label: '白城市' },
  { value: '222400', label: '延边朝鲜族自治州' },
  { value: '230100', label: '哈尔滨市' },
  { value: '230200', label: '齐齐哈尔市' },
  { value: '230300', label: '鸡西市' },
  { value: '230400', label: '鹤岗市' },
  { value: '230500', label: '双鸭山市' },
  { value: '230600', label: '大庆市' },
  { value: '230700', label: '伊春市' },
  { value: '230800', label: '佳木斯市' },
  { value: '230900', label: '七台河市' },
  { value: '231000', label: '牡丹江市' },
  { value: '231100', label: '黑河市' },
  { value: '231200', label: '绥化市' },
  { value: '232700', label: '大兴安岭地区' },
  { value: '310100', label: '上海市市辖区' },
  { value: '320100', label: '南京市' },
  { value: '320200', label: '无锡市' },
  { value: '320300', label: '徐州市' },
  { value: '320400', label: '常州市' },
  { value: '320500', label: '苏州市' },
  { value: '320600', label: '南通市' },
  { value: '320700', label: '连云港市' },
  { value: '320800', label: '淮安市' },
  { value: '320900', label: '盐城市' },
  { value: '321000', label: '扬州市' },
  { value: '321100', label: '镇江市' },
  { value: '321200', label: '泰州市' },
  { value: '321300', label: '宿迁市' },
  { value: '330100', label: '杭州市' },
  { value: '330200', label: '宁波市' },
  { value: '330300', label: '温州市' },
  { value: '330400', label: '嘉兴市' },
  { value: '330500', label: '湖州市' },
  { value: '330600', label: '绍兴市' },
  { value: '330700', label: '金华市' },
  { value: '330800', label: '衢州市' },
  { value: '330900', label: '舟山市' },
  { value: '331000', label: '台州市' },
  { value: '331100', label: '丽水市' },
  { value: '340100', label: '合肥市' },
  { value: '340200', label: '芜湖市' },
  { value: '340300', label: '蚌埠市' },
  { value: '340400', label: '淮南市' },
  { value: '340500', label: '马鞍山市' },
  { value: '340600', label: '淮北市' },
  { value: '340700', label: '铜陵市' },
  { value: '340800', label: '安庆市' },
  { value: '341000', label: '黄山市' },
  { value: '341100', label: '滁州市' },
  { value: '341200', label: '阜阳市' },
  { value: '341300', label: '宿州市' },
  { value: '341500', label: '六安市' },
  { value: '341600', label: '亳州市' },
  { value: '341700', label: '池州市' },
  { value: '341800', label: '宣城市' },
  { value: '350100', label: '福州市' },
  { value: '350200', label: '厦门市' },
  { value: '350300', label: '莆田市' },
  { value: '350400', label: '三明市' },
  { value: '350500', label: '泉州市' },
  { value: '350600', label: '漳州市' },
  { value: '350700', label: '南平市' },
  { value: '350800', label: '龙岩市' },
  { value: '350900', label: '宁德市' },
  { value: '360100', label: '南昌市' },
  { value: '360200', label: '景德镇市' },
  { value: '360300', label: '萍乡市' },
  { value: '360400', label: '九江市' },
  { value: '360500', label: '新余市' },
  { value: '360600', label: '鹰潭市' },
  { value: '360700', label: '赣州市' },
  { value: '360800', label: '吉安市' },
  { value: '360900', label: '宜春市' },
  { value: '361000', label: '抚州市' },
  { value: '361100', label: '上饶市' },
  { value: '370100', label: '济南市' },
  { value: '370200', label: '青岛市' },
  { value: '370300', label: '淄博市' },
  { value: '370400', label: '枣庄市' },
  { value: '370500', label: '东营市' },
  { value: '370600', label: '烟台市' },
  { value: '370700', label: '潍坊市' },
  { value: '370800', label: '济宁市' },
  { value: '370900', label: '泰安市' },
  { value: '371000', label: '威海市' },
  { value: '371100', label: '日照市' },
  { value: '371300', label: '临沂市' },
  { value: '371400', label: '德州市' },
  { value: '371500', label: '聊城市' },
  { value: '371600', label: '滨州市' },
  { value: '371700', label: '菏泽市' },
  { value: '410100', label: '郑州市' },
  { value: '410200', label: '开封市' },
  { value: '410300', label: '洛阳市' },
  { value: '410400', label: '平顶山市' },
  { value: '410500', label: '安阳市' },
  { value: '410600', label: '鹤壁市' },
  { value: '410700', label: '新乡市' },
  { value: '410800', label: '焦作市' },
  { value: '410900', label: '濮阳市' },
  { value: '411000', label: '许昌市' },
  { value: '411100', label: '漯河市' },
  { value: '411200', label: '三门峡市' },
  { value: '411300', label: '南阳市' },
  { value: '411400', label: '商丘市' },
  { value: '411500', label: '信阳市' },
  { value: '411600', label: '周口市' },
  { value: '411700', label: '驻马店市' },
  { value: '419001', label: '济源市' },
  { value: '420100', label: '武汉市' },
  { value: '420200', label: '黄石市' },
  { value: '420300', label: '十堰市' },
  { value: '420500', label: '宜昌市' },
  { value: '420600', label: '襄阳市' },
  { value: '420700', label: '鄂州市' },
  { value: '420800', label: '荆门市' },
  { value: '420900', label: '孝感市' },
  { value: '421000', label: '荆州市' },
  { value: '421100', label: '黄冈市' },
  { value: '421200', label: '咸宁市' },
  { value: '421300', label: '随州市' },
  { value: '422800', label: '恩施土家族苗族自治州' },
  { value: '429004', label: '仙桃市' },
  { value: '429005', label: '潜江市' },
  { value: '429006', label: '天门市' },
  { value: '429021', label: '神农架林区' },
  { value: '430100', label: '长沙市' },
  { value: '430200', label: '株洲市' },
  { value: '430300', label: '湘潭市' },
  { value: '430400', label: '衡阳市' },
  { value: '430500', label: '邵阳市' },
  { value: '430600', label: '岳阳市' },
  { value: '430700', label: '常德市' },
  { value: '430800', label: '张家界市' },
  { value: '430900', label: '益阳市' },
  { value: '431000', label: '郴州市' },
  { value: '431100', label: '永州市' },
  { value: '431200', label: '怀化市' },
  { value: '431300', label: '娄底市' },
  { value: '433100', label: '湘西土家族苗族自治州' },
  { value: '440100', label: '广州市' },
  { value: '440200', label: '韶关市' },
  { value: '440300', label: '深圳市' },
  { value: '440400', label: '珠海市' },
  { value: '440500', label: '汕头市' },
  { value: '440600', label: '佛山市' },
  { value: '440700', label: '江门市' },
  { value: '440800', label: '湛江市' },
  { value: '440900', label: '茂名市' },
  { value: '441200', label: '肇庆市' },
  { value: '441300', label: '惠州市' },
  { value: '441400', label: '梅州市' },
  { value: '441500', label: '汕尾市' },
  { value: '441600', label: '河源市' },
  { value: '441700', label: '阳江市' },
  { value: '441800', label: '清远市' },
  { value: '441900', label: '东莞市' },
  { value: '442000', label: '中山市' },
  { value: '445100', label: '潮州市' },
  { value: '445200', label: '揭阳市' },
  { value: '445300', label: '云浮市' },
  { value: '450100', label: '南宁市' },
  { value: '450200', label: '柳州市' },
  { value: '450300', label: '桂林市' },
  { value: '450400', label: '梧州市' },
  { value: '450500', label: '北海市' },
  { value: '450600', label: '防城港市' },
  { value: '450700', label: '钦州市' },
  { value: '450800', label: '贵港市' },
  { value: '450900', label: '玉林市' },
  { value: '451000', label: '百色市' },
  { value: '451100', label: '贺州市' },
  { value: '451200', label: '河池市' },
  { value: '451300', label: '来宾市' },
  { value: '451400', label: '崇左市' },
  { value: '460100', label: '海口市' },
  { value: '460200', label: '三亚市' },
  { value: '460300', label: '三沙市' },
  { value: '460400', label: '儋州市' },
  { value: '469001', label: '五指山市' },
  { value: '469002', label: '琼海市' },
  { value: '469005', label: '文昌市' },
  { value: '469006', label: '万宁市' },
  { value: '469007', label: '东方市' },
  { value: '469021', label: '定安县' },
  { value: '469022', label: '屯昌县' },
  { value: '469023', label: '澄迈县' },
  { value: '469024', label: '临高县' },
  { value: '469025', label: '白沙黎族自治县' },
  { value: '469026', label: '昌江黎族自治县' },
  { value: '469027', label: '乐东黎族自治县' },
  { value: '469028', label: '陵水黎族自治县' },
  { value: '469029', label: '保亭黎族苗族自治县' },
  { value: '469030', label: '琼中黎族苗族自治县' },
  { value: '500100', label: '重庆市市辖区' },
  { value: '500200', label: '重庆市郊县' },
  { value: '510100', label: '成都市' },
  { value: '510300', label: '自贡市' },
  { value: '510400', label: '攀枝花市' },
  { value: '510500', label: '泸州市' },
  { value: '510600', label: '德阳市' },
  { value: '510700', label: '绵阳市' },
  { value: '510800', label: '广元市' },
  { value: '510900', label: '遂宁市' },
  { value: '511000', label: '内江市' },
  { value: '511100', label: '乐山市' },
  { value: '511300', label: '南充市' },
  { value: '511400', label: '眉山市' },
  { value: '511500', label: '宜宾市' },
  { value: '511600', label: '广安市' },
  { value: '511700', label: '达州市' },
  { value: '511800', label: '雅安市' },
  { value: '511900', label: '巴中市' },
  { value: '512000', label: '资阳市' },
  { value: '513200', label: '阿坝藏族羌族自治州' },
  { value: '513300', label: '甘孜藏族自治州' },
  { value: '513400', label: '凉山彝族自治州' },
  { value: '520100', label: '贵阳市' },
  { value: '520200', label: '六盘水市' },
  { value: '520300', label: '遵义市' },
  { value: '520400', label: '安顺市' },
  { value: '520500', label: '毕节市' },
  { value: '520600', label: '铜仁市' },
  { value: '522300', label: '黔西南布依族苗族自治州' },
  { value: '522600', label: '黔东南苗族侗族自治州' },
  { value: '522700', label: '黔南布依族苗族自治州' },
  { value: '530100', label: '昆明市' },
  { value: '530300', label: '曲靖市' },
  { value: '530400', label: '玉溪市' },
  { value: '530500', label: '保山市' },
  { value: '530600', label: '昭通市' },
  { value: '530700', label: '丽江市' },
  { value: '530800', label: '普洱市' },
  { value: '530900', label: '临沧市' },
  { value: '532300', label: '楚雄彝族自治州' },
  { value: '532500', label: '红河哈尼族彝族自治州' },
  { value: '532600', label: '文山壮族苗族自治州' },
  { value: '532800', label: '西双版纳傣族自治州' },
  { value: '532900', label: '大理白族自治州' },
  { value: '533100', label: '德宏傣族景颇族自治州' },
  { value: '533300', label: '怒江傈僳族自治州' },
  { value: '533400', label: '迪庆藏族自治州' },
  { value: '540100', label: '拉萨市' },
  { value: '540200', label: '日喀则市' },
  { value: '540300', label: '昌都市' },
  { value: '540400', label: '林芝市' },
  { value: '540500', label: '山南市' },
  { value: '540600', label: '那曲市' },
  { value: '542500', label: '阿里地区' },
  { value: '610100', label: '西安市' },
  { value: '610200', label: '铜川市' },
  { value: '610300', label: '宝鸡市' },
  { value: '610400', label: '咸阳市' },
  { value: '610500', label: '渭南市' },
  { value: '610600', label: '延安市' },
  { value: '610700', label: '汉中市' },
  { value: '610800', label: '榆林市' },
  { value: '610900', label: '安康市' },
  { value: '611000', label: '商洛市' },
  { value: '620100', label: '兰州市' },
  { value: '620200', label: '嘉峪关市' },
  { value: '620300', label: '金昌市' },
  { value: '620400', label: '白银市' },
  { value: '620500', label: '天水市' },
  { value: '620600', label: '武威市' },
  { value: '620700', label: '张掖市' },
  { value: '620800', label: '平凉市' },
  { value: '620900', label: '酒泉市' },
  { value: '621000', label: '庆阳市' },
  { value: '621100', label: '定西市' },
  { value: '621200', label: '陇南市' },
  { value: '622900', label: '临夏回族自治州' },
  { value: '623000', label: '甘南藏族自治州' },
  { value: '630100', label: '西宁市' },
  { value: '630200', label: '海东市' },
  { value: '632200', label: '海北藏族自治州' },
  { value: '632300', label: '黄南藏族自治州' },
  { value: '632500', label: '海南藏族自治州' },
  { value: '632600', label: '果洛藏族自治州' },
  { value: '632700', label: '玉树藏族自治州' },
  { value: '632800', label: '海西蒙古族藏族自治州' },
  { value: '640100', label: '银川市' },
  { value: '640200', label: '石嘴山市' },
  { value: '640300', label: '吴忠市' },
  { value: '640400', label: '固原市' },
  { value: '640500', label: '中卫市' },
  { value: '650100', label: '乌鲁木齐市' },
  { value: '650200', label: '克拉玛依市' },
  { value: '650400', label: '吐鲁番市' },
  { value: '650500', label: '哈密市' },
  { value: '652300', label: '昌吉回族自治州' },
  { value: '652700', label: '博尔塔拉蒙古自治州' },
  { value: '652800', label: '巴音郭楞蒙古自治州' },
  { value: '652900', label: '阿克苏地区' },
  { value: '653000', label: '克孜勒苏柯尔克孜自治州' },
  { value: '653100', label: '喀什地区' },
  { value: '653200', label: '和田地区' },
  { value: '654000', label: '伊犁哈萨克自治州' },
  { value: '654200', label: '塔城地区' },
  { value: '654300', label: '阿勒泰地区' },
  { value: '659001', label: '石河子市' },
  { value: '659002', label: '阿拉尔市' },
  { value: '659003', label: '图木舒克市' },
  { value: '659004', label: '五家渠市' },
  { value: '659005', label: '北屯市' },
  { value: '659006', label: '铁门关市' },
  { value: '659007', label: '双河市' },
  { value: '659008', label: '可克达拉市' },
  { value: '659009', label: '昆玉市' },
  { value: '810001', label: '中西区' },
  { value: '810002', label: '湾仔区' },
  { value: '810003', label: '东区' },
  { value: '810004', label: '南区' },
  { value: '810005', label: '油尖旺区' },
  { value: '810006', label: '深水埗区' },
  { value: '810007', label: '九龙城区' },
  { value: '810008', label: '黄大仙区' },
  { value: '810009', label: '观塘区' },
  { value: '810010', label: '荃湾区' },
  { value: '810011', label: '屯门区' },
  { value: '810012', label: '元朗区' },
  { value: '810013', label: '北区' },
  { value: '810014', label: '大埔区' },
  { value: '810015', label: '西贡区' },
  { value: '810016', label: '沙田区' },
  { value: '810017', label: '葵青区' },
  { value: '810018', label: '离岛区' },
  { value: '820001', label: '花地玛堂区' },
  { value: '820002', label: '花王堂区' },
  { value: '820003', label: '望德堂区' },
  { value: '820004', label: '大堂区' },
  { value: '820005', label: '风顺堂区' },
  { value: '820006', label: '嘉模堂区' },
  { value: '820007', label: '路凼填海区' },
  { value: '820008', label: '圣方济各堂区' }
]
selectUtil.sqlType = [
  { value: 1, label: '标准sql' },
  { value: 2, label: '存储过程' }
]
selectUtil.symbol = [
  { value: 'circle', label: 'circle' },
  { value: 'rect', label: 'rect' },
  { value: 'roundRect', label: 'roundRect' },
  { value: 'triangle', label: 'triangle' },
  { value: 'diamond', label: 'diamond' },
  { value: 'arrow', label: 'arrow' },
  { value: 'pin', label: 'pin' }
]
selectUtil.mapLabelPosition = [
  { value: 'top', label: 'top' },
  { value: 'left', label: 'left' },
  { value: 'right', label: 'right' },
  { value: 'bottom', label: 'bottom' },
  { value: 'inside', label: 'inside' }
]
selectUtil.symbolSizeType = [
  { value: '1', label: '固定值' },
  { value: '2', label: '根据数量显示不同大小' }
]

selectUtil.lineSymbol = [
  { value: 'emptyCircle', label: 'emptyCircle' },
  { value: 'circle', label: 'circle' },
  { value: 'rect', label: 'rect' },
  { value: 'roundRect', label: 'roundRect' },
  { value: 'triangle', label: 'triangle' },
  { value: 'diamond', label: 'diamond' },
  { value: 'arrow', label: 'arrow' },
  { value: 'pin', label: 'pin' }
]
selectUtil.chartType = [
  { value: 'line', label: '折线' },
  { value: 'bar', label: '柱状' }
]

selectUtil.componentType = [
  { value: 'text', label: '文本' },
  { value: 'picture', label: '图片' },
  { value: 'histogram', label: '柱状图' },
  { value: 'lineChart', label: '折线图' },
  { value: 'histogramLineChart', label: '折柱图' },
  { value: 'pieChart', label: '饼图' },
  { value: 'gauge', label: '仪表盘' },
  // {value:"table",label:"表格"},
  { value: 'date', label: '日期' },
  { value: 'map', label: '地图' },
  { value: 'map3d', label: '3D地图' },
  { value: 'mapScatter', label: '散点地图' },
  { value: 'mapMigrate', label: '迁徙地图' },
  { value: 'pie3dChart', label: '3D饼图' },
  { value: 'circleProgress', label: '环形百分比图' },
  { value: 'progressBar', label: '进度条' }
]
selectUtil.viewAuth = [
  { value: 1, label: '所有人' },
  { value: 2, label: '指定角色' }
]

selectUtil.designAuth = [
  { value: 1, label: '所有人可设计' },
  { value: 2, label: '输入密码后设计' }
]

selectUtil.clickType = [
  { value: '1', label: '关闭' },
  { value: '2', label: '下钻' },
  { value: '3', label: '外部链接' }
]

selectUtil.drillComponentType = [
  { value: 'histogram', label: '柱状图' },
  { value: 'lineChart', label: '折线图' },
  { value: 'histogramLineChart', label: '折柱图' },
  { value: 'pieChart', label: '饼图' },
  { value: 'gauge', label: '仪表盘' },
  { value: 'map', label: '地图' },
  // {value:"map3d",label:"3D地图"},
  { value: 'mapScatter', label: '散点地图' }
  // {value:"mapMigrate",label:"迁徙地图"},
  // {value:"pie3dChart",label:"3D饼图"},
  // {value:"circleProgress",label:"环形百分比图"},
]
selectUtil.valueType = [
  { value: 1, label: '文本' },
  { value: 2, label: '数值' },
  { value: 3, label: '日期' }
]

selectUtil.tplType = [
  { value: 1, label: '展示报表' },
  { value: 2, label: '填报报表' }
]

selectUtil.lineargardient = [
  { value: 'bottom', label: '从上到下' },
  { value: 'top', label: '从下到上' },
  { value: 'right', label: '从左至右' },
  { value: 'left', label: '从右向左' },
  { value: 'bottom right', label: '从左上至右下' },
  { value: 'bottom left', label: '从右上至左下' }
]

selectUtil.radarShapes = [
  { value: 'polygon', label: 'polygon' },
  { value: 'circle', label: 'circle' }
]

selectUtil.borderTypes = [
  { value: 'border1.png', label: '边框1' },
  { value: 'border2.png', label: '边框2' },
  { value: 'border3.png', label: '边框3' },
  { value: 'border4.png', label: '边框4' },
  { value: 'border5.png', label: '边框5' },
  { value: 'border6.png', label: '边框6' },
  { value: 'border7.png', label: '边框7' },
  { value: 'border8.png', label: '边框8' },
  { value: 'border9.png', label: '边框9' },
  { value: 'border10.png', label: '边框10' },
  { value: 'border11.png', label: '边框11' },
  { value: 'border12.png', label: '边框12' },
  { value: 'border13.png', label: '边框13' },
  { value: 'border14.png', label: '边框14' },
  { value: 'border15.png', label: '边框15' },
  { value: 'border16.png', label: '边框16' },
  { value: 'border17.png', label: '边框17' },
  { value: 'border18.png', label: '边框18' },
  { value: 'border19.png', label: '边框19' },
  { value: 'border20.png', label: '边框20' },
  { value: 'border21.png', label: '边框21' },
  { value: 'border22.png', label: '边框22' },
  { value: 'border23.png', label: '边框23' },
  { value: 'border24.png', label: '边框24' },
  { value: 'border25.png', label: '边框25' },
  { value: 'border26.png', label: '边框26' },
  { value: 'border27.png', label: '边框27' },
  { value: 'border28.png', label: '边框28' },
  { value: 'border29.png', label: '边框29' },
  { value: 'border30.png', label: '边框30' },
  { value: 'border31.png', label: '边框31' },
  { value: 'border32.png', label: '边框32' },
  { value: 'border33.png', label: '边框33' },
  { value: 'border34.png', label: '边框34' },
  { value: 'border35.png', label: '边框35' },
  { value: 'border36.png', label: '边框36' }
]

selectUtil.imgType = [
  { value: 1, label: '自定义' },
  { value: 2, label: '系统背景' }
]
selectUtil.backgroundTypes = [
  // {value:"bg1.png",label:"背景1(1920x1080)"},
  // {value:"bg2.jpg",label:"背景2(2560x1440)"},
  // {value:"bg3.png",label:"背景3(1920x1080)"},
  // {value:"bg4.png",label:"背景4(1966x1110)"},
  // {value:"bg5.png",label:"背景5(1966x1110)"},
  // {value:"bg6.png",label:"背景6(1920x1080)"},
  // {value:"bg7.png",label:"背景7(1920x1080)"},
  // {value:"bg8.png",label:"背景8(1920x1080)"},
  // {value:"bg9.png",label:"背景9(1920x1080)"},
  // {value:"bg10.png",label:"背景10(1920x1080)"},
  // {value:"bg11.png",label:"背景11(1920x1080)"},
  // {value:"bg12.png",label:"背景12(1920x1080)"},
  // {value:"bg13.jpg",label:"背景13(1920x1080)"},
  // {value:"bg14.jpg",label:"背景14(1920x1080)"},
  // {value:"bg15.jpg",label:"背景15(1920x1080)"},
  // {value:"bg16.png",label:"背景16(1600x800)"},
  // {value:"bg17.jpg",label:"背景17(1920x1080)"},
  // {value:"bg18.jpg",label:"背景18(1920x1080)"},
  // {value:"bg19.png",label:"背景19(3840x2160)"},
  // {value:"bg20.png",label:"背景20(1920x1080)"},
  // {value:"bg21.png",label:"背景21(1920x1080)"},
  // {value:"bg22.png",label:"背景22(1920x1080)"},
  // {value:"bg23.png",label:"背景23(1920x1080)"},
  // {value:"bg24.jpg",label:"背景24(4096x2160)"},
  // {value:"bg25.png",label:"背景25(1920x1080)"},
  // {value:"bg26.png",label:"背景26(1920x1080)"},
  // {value:"bg27.jpg",label:"背景27(1920x1080)"},
  // {value:"bg28.png",label:"背景28(1920x1080)"},
  // {value:"bg29.png",label:"背景29(1920x1080)"},
  // {value:"bg30.jpg",label:"背景30(1920x1080)"},
]

selectUtil.requestType = [
  { value: 'post', label: 'post' },
  { value: 'get', label: 'get' }
]

selectUtil.pageCount = [
  { value: 5, label: 5 },
  { value: 10, label: 10 },
  { value: 15, label: 15 },
  { value: 20, label: 20 },
  { value: 30, label: 30 },
  { value: 50, label: 50 },
  { value: 100, label: 100 },
  { value: 150, label: 150 },
  { value: 200, label: 200 },
  { value: 300, label: 300 },
  { value: 500, label: 500 },
  { value: 800, label: 800 },
  { value: 1000, label: 1000 },
  { value: 2000, label: 2000 },
  { value: 3000, label: 3000 },
  { value: 4000, label: 4000 },
  { value: 5000, label: 5000 }
]

selectUtil.operate = [
  { value: '=', label: '=' },
  { value: '!=', label: '!=' },
  { value: '>', label: '>' },
  { value: '>=', label: '>=' },
  { value: '<', label: '<' },
  { value: '<=', label: '<=' },
  { value: 'in', label: 'in(包含)' },
  { value: 'not in', label: 'not in(不包含)' }
]
selectUtil.type = [
  { value: 'varchar', label: '字符串' },
  { value: 'number', label: '数字' },
  { value: 'date', label: '日期' },
  { value: 'cell', label: '单元格' }
]

selectUtil.type2 = [
  { value: 'varchar', label: '字符串' },
  { value: 'number', label: '数字' },
  { value: 'date', label: '日期' }
]

selectUtil.dateFormat2 = [
  { value: 'yyyy-MM-dd', label: 'yyyy-MM-dd' },
  { value: 'yyyy-MM-dd HH:mm:ss', label: 'yyyy-MM-dd HH:mm:ss' },
  { value: 'yyyy-MM-dd HH:mm', label: 'yyyy-MM-dd HH:mm' }
]

selectUtil.xAxisDataType = [
  { value: 1, label: '自定义' },
  { value: 2, label: '数据集' }
]

selectUtil.cellType = [
  { value: 1, label: '固定单元格' },
  { value: 2, label: '同行/列扩展单元格' }
]

selectUtil.shareType = [
  { value: 1, label: 'pc' },
  { value: 2, label: 'h5' }
]

selectUtil.shareTime = [
  { value: 1, label: '永久有效' },
  { value: 2, label: '自定义时长' }
]

selectUtil.reportExportType = [
  { value: 1, label: 'excel' },
  { value: 2, label: 'pdf' },
  { value: 3, label: 'excel和pdf' }
]

selectUtil.jobTimeType = [
  { value: 1, label: '指定时间' },
  { value: 2, label: 'cron表达式' }
]
selectUtil.dataLoadType = [
  { value: 1, label: '加载全部数据' },
  { value: 2, label: '点击sheet加载数据' }
]

selectUtil.pageType = [
  { value: 1, label: 'A3' },
  { value: 2, label: 'A4' },
  { value: 3, label: 'A5' },
  { value: 4, label: 'A6' },
  { value: 5, label: 'B2' },
  { value: 6, label: 'B3' },
  { value: 7, label: 'B4' },
  { value: 8, label: 'B5' },
  { value: 9, label: 'letter' },
  { value: 10, label: 'legal' }
]
selectUtil.pageLayout = [
  { value: 1, label: '纵向' },
  { value: 2, label: '横向' }
]
selectUtil.pdfPosition = [
  { value: 1, label: '左' },
  { value: 2, label: '中' },
  { value: 3, label: '右' }
]
selectUtil.waterMarkType = [
  { value: 1, label: '文本' },
  { value: 2, label: '图片' }
]
selectUtil.status = [
  { value: 1, label: '启用' },
  { value: 2, label: '禁用' }
]

selectUtil.subtotalType = [
  { value: '1', label: '合计' },
  { value: '2', label: '平均值' },
  { value: '3', label: '最大值' },
  { value: '4', label: '最小值' },
  { value: '5', label: '计数' },
  { value: '6', label: '同比/环比差值' },
  { value: '7', label: '同比/环比增长率' }
]

selectUtil.fontSize = [
  { value: 9, label: '9' },
  { value: 10, label: '10' },
  { value: 11, label: '11' },
  { value: 12, label: '12' },
  { value: 14, label: '14' },
  { value: 16, label: '16' },
  { value: 18, label: '18' },
  { value: 20, label: '20' },
  { value: 22, label: '22' },
  { value: 24, label: '24' },
  { value: 26, label: '26' },
  { value: 28, label: '28' },
  { value: 30, label: '30' }
]

selectUtil.docChartType = [
  { value: 'pie', label: '饼图' },
  { value: 'histogram', label: '柱状图' },
  { value: 'horizontalHistogram', label: '条形图' },
  { value: 'line', label: '折线图' }
]

selectUtil.highlightcolor = [
  { value: '#', label: '无色' },
  { value: '#FFFF00', label: '黄色' },
  { value: '#00FF00', label: '绿色' },
  { value: '#00FFFF', label: '青色' },
  { value: '#FF00FF', label: '粉红色' },
  { value: '#0000FF', label: '蓝色' },
  { value: '#FF0000', label: '红色' },
  { value: '#000080', label: '深蓝色' },
  { value: '#008080', label: '深青色' },
  { value: '#008000', label: '深绿色' },
  { value: '#800080', label: '紫色' },
  { value: '#800000', label: '深红色' },
  { value: '#808000', label: '深黄色' },
  { value: '#808080', label: '深灰色' },
  { value: '#C0C0C0', label: '浅灰色' },
  { value: '#000000', label: '黑色' }
]
selectUtil.luckyChartType = [
  { value: 'echarts|column|default', label: '柱状图', img: 'bar-simple.webp' },
  { value: 'echarts|column|stack', label: '堆叠柱状图', img: 'bar-stack.webp' },
  { value: 'echarts|bar|default', label: '条形图', img: 'bar-y-category.webp' },
  { value: 'echarts|bar|stack', label: '堆叠条形图', img: 'bar-y-category-stack.webp' },
  { value: 'echarts|line|default', label: '折线图', img: 'line-simple.webp' },
  { value: 'echarts|line|smooth', label: '平滑折线图', img: 'line-smooth.webp' },
  { value: 'echarts|area|default', label: '面积图', img: 'area-basic.webp' },
  { value: 'echarts|area|stack', label: '堆叠面积图', img: 'area-stack.webp' },
  { value: 'echarts|pie|default', label: '饼图', img: 'pie-simple.webp' },
  { value: 'echarts|radar|default', label: '雷达图', img: 'radar.webp' }
]

selectUtil.cellFillType = [
  { value: 1, label: '插入' },
  { value: 2, label: '覆盖' }
]

selectUtil.borderType = [
  {"name":"dv-border-box-1","text":"边框1"},
  {"name":"dv-border-box-2","text":"边框2"},
  {"name":"dv-border-box-3","text":"边框3"},
  {"name":"dv-border-box-4","text":"边框4"},
  {"name":"dv-border-box-4-reverse","text":"边框4(反向)"},
  {"name":"dv-border-box-5","text":"边框5"},
  {"name":"dv-border-box-5-reverse","text":"边框5(反向)"},
  {"name":"dv-border-box-6","text":"边框6"},
  {"name":"dv-border-box-7","text":"边框7"},
  {"name":"dv-border-box-8","text":"边框8"},
  {"name":"dv-border-box-8-reverse","text":"边框8(反向)"},
  {"name":"dv-border-box-9","text":"边框9"},
  {"name":"dv-border-box-10","text":"边框10"},
  {"name":"dv-border-box-11","text":"边框11"},
  {"name":"dv-border-box-12","text":"边框12"},
  {"name":"dv-border-box-13","text":"边框13"},
]

selectUtil.mongoSearchType = [
  { value: 1, name: '查询文档(find)' },
  { value: 2, name: '聚合查询(aggregate)' }
]

selectUtil.searchFormType = [
  { value: 1, label: '页面顶部' },
  { value: 2, label: '侧边栏弹出' }
]

export default selectUtil
