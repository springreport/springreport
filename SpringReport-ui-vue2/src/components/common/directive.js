// Vue自定义指令

// dialog可以拖来拖去
const directive = Vue => {
    Vue.directive('drag2anywhere', {
    // 当被绑定的元素插入到 DOM 中时……
        inserted: (el) => {
            el.style.overflow = 'auto';
            const target = el.childNodes[0];
            const targetHeader = target.childNodes[0];
            let isOnclick = false;
            let mouseX;
            let mouseY;
            let objX;
            let objY;
            targetHeader.onmousedown = (event) => {
                isOnclick = true;
                target.style.cursor = 'move';
                target.style.left = target.offsetLeft + 'px'; // 先记录之前的位置
                target.style.top = target.offsetTop + 'px'; // 先记录之前的位置
                target.style.margin = '0'; // 去掉margin
                mouseX = event.clientX;
                mouseY = event.clientY;
                objX = parseInt(target.style.left || target.offsetLeft);
                objY = parseInt(target.style.top || target.offsetTop);
                document.onmousemove = null; // 解绑监听鼠标拖动事件
                document.onmousemove = (event) => { // 监听鼠标拖动事件
                    if (isOnclick) {
                        event = event || window.event;
                        target.style.left = parseInt(event.clientX - mouseX + objX) + 'px';
                        target.style.top = parseInt(event.clientY - mouseY + objY) + 'px';
                    }
                };
            };
            targetHeader.onmouseup = (event) => { // 松开鼠标
                isOnclick = false;
                target.style.cursor = 'default';
            };
        }
    });
};
export default directive;