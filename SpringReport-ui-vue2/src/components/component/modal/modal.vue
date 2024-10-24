<template>
    <div>
        <el-dialog
        :title="modalConfig.title"
        :visible.sync="modalConfig.show"
        :width="modalConfig.width"
        :close-on-click-modal='false'
        @close="closeModal"
        append-to-body
        >
        <div class="el-dialog-div">
            <el-form :inline="true" ref="modalFormRef" class="demo-form-inline" :model="modalData" :disabled="modalConfig.formEditDisabled" autocomplete='modalConfig.autocomplete' :label-width="labelWidth" >
                <el-form-item v-for='item in modalForm' :class="itemClass" v-show="item.show==undefined?true:item.show"  :label="item.label" :key='item.prop' :prop="item.prop" :rules="filter_rules(item.label,item.rules)" :label-width="item.labelWidth">
                    <!-- 输入框 -->
                    <el-input v-if="item.type==='Input'" :style="'width:' + item.width" v-model="modalData[item.prop]" :placeholder="'请输入'+item.label" size="item.size" :clearable="item.clearable" :disabled="item.disabled && item.disabled(modalData[item.prop])" @clear="item.clear && item.clear(modalData[item.prop])" @change="item.change && item.change(modalData[item.prop],item.params)" @input="item.input && item.input(modalData[item.prop])"  @focus="item.focus && item.focus(modalData[item.prop])"></el-input>
                    <el-input v-if="item.type==='Password'" type="password" :show-password="false" :style="'width:' + item.width" v-model="modalData[item.prop]" :placeholder="'请输入'+item.label" size="item.size" :clearable="item.clearable" :disabled="item.disabled && item.disabled(modalData[item.prop])" @clear="item.clear && item.clear(modalData[item.prop])" @change="item.change && item.change(modalData[item.prop],item.params)" @input="item.input && item.input(modalData[item.prop])"  @focus="item.focus && item.focus(modalData[item.prop])"></el-input>
                    <el-col v-if="item.type==='Line'" class="line" :span="2">-</el-col>
                    <el-input v-if="item.type==='Input2'" :style="'width:' + item.width" v-model="modalData[item.prop]" :placeholder="'请输入'+item.title" size="item.size" :clearable="item.clearable" :disabled="item.disabled && item.disabled(modalData[item.prop])" @clear="item.clear && item.clear(modalData[item.prop])" @change="item.change && item.change(modalData[item.prop],item.params)" @input="item.input && item.input(modalData[item.prop])"  @focus="item.focus && item.focus(modalData[item.prop])"></el-input>
                    <cron v-if="item.cron" @change="item.changeCron" @close="item.cron=false" i18n="cn"></cron>
                    <!-- 下拉框 -->
                    <el-select :ref="item.ref" :style="'width:' + item.width" v-if="item.type==='Select'" :multiple='item.multiple' :filterable="item.remote" :remote="item.remote" :remote-method="item.remoteMethod" v-model="modalData[item.prop]" size="item.size" :disabled="item.disabled && item.disabled(modalData[item.prop])" @change="item.change && item.change(modalData[item.prop],item.params)" @focus="item.focus && item.focus(modalData[item.prop],item.params)">
                        <el-option v-if="!item.multiple" :label="'请选择'+item.label" value=""></el-option>
                        <el-option v-for="op in item.options" :label="item.props?op[item.props.label]:op.label" :value="item.props?op[item.props.value]:op.value" :key="item.props?op[item.props.value]:op.value"></el-option>
                    </el-select>
                    <!-- 单选 -->
                    <el-radio-group :style="'width:' + item.width" v-if="item.type==='Radio'" v-model="modalData[item.prop]" @change="item.change && item.change(modalData[item.prop],item.params)" :disabled="item.disabled">
                        <el-radio v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio>
                    </el-radio-group>
                     <!-- 单选按钮 -->
                    <el-radio-group :style="'width:' + item.width" v-if="item.type==='RadioButton'" v-model="modalData[item.prop]" @change="item.change && item.change(modalData[item.prop],item.params)">
                        <el-radio-button v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio-button>
                    </el-radio-group>
                    <!-- 复选框 -->
                    <el-checkbox-group :style="'width:' + item.width" v-if="item.type==='Checkbox'" v-model="modalData[item.prop]" @change="item.change && item.change(modalData[item.prop],item.params)">
                        <el-checkbox v-for="ch in item.checkboxs" :label="ch.value" :key="ch.value">{{ch.label}}</el-checkbox>
                    </el-checkbox-group>
                    <!-- 日期 -->
                    <el-date-picker :style="'width:' + item.width" v-if="item.type==='Date'" v-model="modalData[item.prop]" format="yyyy-MM-dd" value-format="yyyy-MM-dd" @change="item.change && item.change(modalData[item.prop],item.params)"></el-date-picker>
                    <!-- 日期范围 -->
                    <el-date-picker :style="'width:' + item.width" v-if="item.type==='DateRange'" type="daterange" range-separator="至" start-placeholder="开始日期"  end-placeholder="结束日期" v-model="modalData[item.prop]" format="yyyy-MM-dd" value-format="yyyy-MM-dd" @change="item.change && item.change(modalData[item.prop],item.params)"></el-date-picker>
                    <!-- 时间 -->
                    <el-time-picker :style="'width:' + item.width" v-if="item.type==='Time'" v-model="modalData[item.prop]" format="HH:mm:ss" value-format="HH:mm:ss" @change="item.change && item.change(modalData[item.prop],item.params)"></el-time-picker>
                    <!-- 时间范围 -->
                    <el-time-picker :style="'width:' + item.width" v-if="item.type==='TimeRange'" v-model="modalData[item.prop]" is-range format="HH:mm:ss" value-format="HH:mm:ss" range-separator="至" start-placeholder="开始时间"  end-placeholder="结束时间" @change="item.change && item.change(modalData[item.prop],item.params)"></el-time-picker>
                    <!-- 日期时间 -->
                    <el-date-picker :style="'width:' + item.width" v-if="item.type==='DateTime'" type='datetime' v-model="modalData[item.prop]" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" :disabled="item.disable && item.disabled(modalData[item.prop])" @change="item.change && item.change(modalData[item.prop],item.params)"></el-date-picker>
                    <!-- 日期范围 -->
                    <el-date-picker :style="'width:' + item.width" v-if="item.type==='DateTimeRange'" type='datetimerange' v-model="modalData[item.prop]" range-separator="至" start-placeholder="开始日期"  end-placeholder="结束日期" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" :disabled="item.disabled && item.disabled(modalData[item.prop])" @change="item.change && item.change(modalData[item.prop],item.params)" :default-time="item.defaultTime"></el-date-picker>
                    <!-- textarea -->
                    <el-input v-if="item.type==='Textarea'" type="textarea" :style="'width:' + item.width" :rows="item.rows?item.rows:2" v-model="modalData[item.prop]" :placeholder="'请输入'+item.label" size="item.size" :disabled="item.disabled && item.disabled(modalData[item.prop])" @change="item.change && item.change(modalData[item.prop],item.params)" @input="item.input && item.input(modalData[item.prop])"  @focus="item.focus && item.focus(modalData[item.prop])" :label-width="item.labelWidth"></el-input>
                    <el-button type="text" v-if="item.type==='Button'" @click="item.click">{{item.buttonText}}</el-button>
                    <!-- 省市区 -->
                    <region v-if="item.type==='Region'" ref="cascaderRegion" v-model="modalData[item.prop]" :areaCode.sync="modalData[item.prop]" :customStyle="'width:' + item.width" :disabled="item.disabled && item.disabled()"></region>
                    <!-- 文件上传 -->
                    <fileUpload :style="'width:' + item.width" v-if="item.type==='Upload'" :fileList.sync="modalData[item.prop]" :accept="item.accept" :tips="item.tips" :limit="item.limit" :multiple="item.multiple" :readonly="item.readonly" :filesize="item.filesize"></fileUpload>
                    <cusTable v-if="item.type==='Table'" 
                    :isIndex='item.isIndex'
                    :isPagination='item.isPagination'
                    :tableCols='item.tableCols' 
                    :tableHandles='item.tableHandles'
                    :tableData='item.tableData'
                    :tablePage='item.tablePage'></cusTable>
                    <div class="sub-title">
                        <a target="_blank" :href="item.suggestionLink">{{item.suggestions}}</a>
                    </div>
                    
                    <!-- 树 -->
                     <el-tree v-if="item.type==='Tree'" 
                        :data="item.data"
                        show-checkbox
                        :default-expand-all="defaultexpandall"
                        :node-key="item.key"
                        :ref="item.ref"
                        :default-checked-keys="item.checked"
                        highlight-current
                        :props="item.props"
                        :style="'width:' + item.width"
                        >
                        </el-tree>
                        <!-- treeselect -->
                        <selectNode v-if="item.type==='TreeSelect'"  :ref="item.ref" v-model="modalData[item.prop]" :props="item.props" :valueId="modalData[item.valueProp]" :options="item.data" :lazy="item.lazy" :url="item.url" :clearable="item.clearable">
                        </selectNode>
                        <multiselectNode v-if="item.type==='MultiTreeSelect'"  :ref="item.ref" v-model="modalData[item.prop]" :props="item.props" :options="item.data" :lazy="item.lazy" :url="item.url">
                        </multiselectNode>
                        <el-divider v-if="item.divider" ></el-divider>
                </el-form-item>
                
            </el-form>
        </div>
            <span slot="footer" class="dialog-footer">
                <el-button 
                v-for='item in modalHandles' 
                :key='item.label'
                :type="item.type" 
                :icon='item.icon' 
                :disabled="modalConfig.disabled && item.disabled()"
                v-show="modalConfig.type!=commonConstants.modalType.detail"
                @click="item.handle()">{{item.label}}</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import {cron} from 'vue-cron';
export default {
    components: { cron },
    props:{
        labelWidth:{
            type:String,
            default:'auto'
        },
        itemClass:{
            type:String,
            default:'form_input'
        },
        modalConfig:{
           type:Object,
           default:()=>({title:'新增',show:false,width:'700px',formEditDisabled:true,autocomplete:false})
        },
        modalFormRef:{
            type:String,
            default:'modalFormRef'
        },
        modalData:{
            type:Object,
            default:() => ({}),
        },
        modalHandles:{
            type:Array,
            default:()=>[]
        },
        modalForm:{
            type:Array,
            default:() => ([]),
        },
        defaultexpandall:{
            type:Boolean,
            default:false,
        },
    },
    name:"modalPage",
    data () {
        return {
        };
    },
    methods:{
        closeModal(){
            this.$emit('closeModal');
        },
    },

}
</script>
<style lang="scss" scoped>
 .el-dialog-div{
     max-height: 60vh;
     overflow: auto;
    }
 ::v-deep .ces-table {
   height: 25vh !important;
}
</style>