import VChart from '@visactor/vchart'
import MarqueeTips from 'vue-marquee-tips'

import textComponent from './draggablecomponent/textComponent.vue'
import borderComponent from './draggablecomponent/borderComponent.vue'
import decorationComponent from './draggablecomponent/decorationComponent.vue'
import tableComponent from './draggablecomponent/tableComponent.vue'
import vchartComponent from './draggablecomponent/vchartComponent.vue'
import numberFlipperComponent from './draggablecomponent/numberFlipperComponent.vue'
import cardListComponent from './draggablecomponent/cardListComponent.vue'
import tabsCardComponent from './draggablecomponent/tabsCardComponent.vue'
export default {
  components: {
    MarqueeTips,
    textComponent,
    borderComponent,
    decorationComponent,
    tableComponent,
    vchartComponent,
    numberFlipperComponent,
    cardListComponent,
    tabsCardComponent
  },
  props: {
    components: {
      type: Array,
      default: () => []
    },
    chartsComponents: {
      type: Object,
      default: () => ({})
    },
    draggable: {
      type: Boolean,
      default: true
    },
    resizable: {
      type: Boolean,
      default: true
    },
    myclass: {
      type: String,
      default: ''
    },
    sendRequest: { // 是否需要动态获取数据，//预览和设计的时候不需要动态获取数据，真正查看的时候才需要
      type: Boolean,
      default: false
    },
    isDesign: { // 是否是设计模式
      type: Boolean,
      default: true
    },
    isCtrl: { // 是否按下ctrl键
      type: Boolean,
      default: false
    },
    viewThat: { // 查看页面的this对象
      type: Object,
      default: () => ({}),
    },
    searchParams: {
      type: Array,
      default: () => []
    },
  },
  data() {
    return {
      activated: null,
      current: null,
      top: 0,
      left: 0,
      multiActivated: []
    }
  },
  methods: {
    onActivated(index, item) {
      if (!this.isDesign) {
        return
      }
      if (this.current) {
          if (!this.isCtrl) {
            this.current.active = false
            this.multiActivated = []
          } else {
            this.multiActivated.push(this.current)
          }
        }
        if (this.isCtrl) {
          this.multiActivated.push(item)
        }

        item.active = true
        // item.zindex = 100;
        this.current = item
        this.$emit('update:activated', item)
        this.$emit('update:isBubblingEvent', true)
        this.keyDown(item)
        // 左侧图层选中
        const layerArr = document.querySelectorAll('.layer-item')
        if (layerArr && layerArr.length) {
          const currentLayerItem = layerArr[index]
          currentLayerItem.scrollIntoView({
            behavior: 'smooth',
            block: 'center',
            inline: 'nearest'
          })
        }
    },
    onDeactivated() {
      document.onkeydown = null;
      if (this.isCtrl) {
        return
      }
      if (this.multiActivated && this.multiActivated.length > 0) {
        var that = this
        setTimeout(() => {
          that.clearMultiActivated('active', false)
        }, 200)
      }
    },
    onResizstop(left, top, width, height) {
      this.current.x = left
      this.current.y = top
      this.current.w = width
      this.current.h = height
      this.$emit('update:isBubblingEvent', true)
    },
    onDragstop(left, top) {
      this.current.x = left
      this.current.y = top
      this.$emit('update:isBubblingEvent', true)
    },
    keyDown(item) {
      document.onkeydown = (e) => {
        // 事件对象兼容
        const e1 =
          e || event || window.event || arguments.callee.caller.arguments[0]
        // 键盘按键判断:左箭头-37;上箭头-38；右箭头-39;下箭头-40
        if (item && item.category != this.screenConstants.category.panel) {
          if (e1 && e1.keyCode == 37) {
            // 按下左箭头
            if (item.x <= 0) {
              return false
            }
            item.x = item.x - 1
            return false
          } else if (e1 && e1.keyCode == 39) {
            // 按下右箭头
            item.x = item.x + 1
            return false
          } else if (e1 && e1.keyCode == 38) {
            // 上箭头
            if (item.y <= 0) {
              return false
            }
            item.y = item.y - 1
            return false
          } else if (e1 && e1.keyCode == 40) {
            // 下箭头
            item.y = item.y + 1
            return false
          }
        }
      }
    },
    // 鼠标右键事件
    onContextmenu(event, item) {
      if (!this.isDesign) {
        return
      }
      this.$contextmenu({
        items: this.getConttextMenu(item),
        event, // 鼠标事件信息
        customClass: 'custom-class', // 自定义菜单 class
        zIndex: 9999, // 菜单样式 z-index
        minWidth: 130 // 主菜单最小宽度
      })
      return false
    },
    getConttextMenu(item) {
      var menus = [
        {
          label: '复制',
          icon: 'icon el-icon-document-copy',
          onClick: () => {
            this.copyItem(item)
          }
        },
        {
          label: '删除',
          icon: 'icon el-icon-delete',
          onClick: () => {
            this.delItem(item)
          }
        }
      ]
      if (!item.locked) {
        menus.push({
          label: '锁定',
          icon: 'icon el-icon-lock',
          onClick: () => {
            this.lockItem(item)
          }
        })
      } else {
        menus.push({
          label: '解锁',
          icon: 'icon el-icon-unlock',
          onClick: () => {
            this.unlockItem(item)
          }
        })
      }
      return menus
    },
    copyItem(item) {
      item.active = false
      const obj = JSON.parse(JSON.stringify(item))
      obj.id = this.commonUtil.getUuid()
      obj.primaryKey = null
      this.activated = obj
      obj.x = obj.x + 10
      obj.y = obj.y + 10
      if(obj.category == this.screenConstants.category.tabsCard){
        if(obj.tabs && obj.tabs.length > 0){
          for (let index = 0; index < obj.tabs.length; index++) {
            const subComponnet = obj.tabs[index].subComponent;
            subComponnet.id = this.commonUtil.getUuid();
          }
        }
      }
      this.components.push(obj)
      if (obj.category == this.screenConstants.category.vchart) {
        this.$nextTick(() => {
          if (obj.type.toLowerCase().indexOf('sankey') >= 0) {
            obj.spec.nodeKey = (datum) => datum.name
          }
          const vchart = new VChart(obj.spec, { dom: obj.id })
          // 绘制
          vchart.renderSync()
          this.chartsComponents[obj.id] = vchart
        })
      }else if(obj.category == this.screenConstants.category.tabsCard){
        var that = this;
          this.$nextTick(() => {
             if(obj.tabs && obj.tabs.length > 0){
              for (let index = 0; index < obj.tabs.length; index++) {
                const subComponnet = obj.tabs[index].subComponent;
                if (subComponnet.category == that.screenConstants.category.vchart) {
                  if (subComponnet.type.toLowerCase().indexOf('sankey') >= 0) {
                    subComponnet.spec.nodeKey = (datum) => datum.name
                  }
                  const vchart = new VChart(subComponnet.spec, { dom: subComponnet.id })
                  // 绘制
                  vchart.renderSync()
                  that.chartsComponents[subComponnet.id] = vchart
                }
              }
            }
            
          })
      }
      this.clearMultiActivated('active', false)
    },
    delItem(item) {
      item.isDelete = true
      this.clearMultiActivated('isDelete', true)
    },
    lockItem(item) {
      item.locked = true
      this.clearMultiActivated('locked', true)
    },
    unlockItem(item) {
      item.locked = false
      this.clearMultiActivated('locked', false)
    },
    realoadScrollTable(component) {
      this.$refs[component.id].reset()
    },
    clearMultiActivated(attr, value) {
      if (this.multiActivated && this.multiActivated.length > 0) {
        for (let index = 0; index < this.multiActivated.length; index++) {
          const element = this.multiActivated[index]
          if (attr != 'active') {
            element.active = false
          }
          element[attr] = value
        }
      }
      this.multiActivated = []
    },
    changeCurrent(item){
      this.current = item;
      this.activated = item
      this.$emit('update:activated', item)
    }
  }
}
