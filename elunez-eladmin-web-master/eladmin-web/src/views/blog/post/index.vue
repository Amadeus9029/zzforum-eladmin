<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.value" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-select v-model="query.type" clearable placeholder="类型" class="filter-item" style="width: 130px">
          <el-option v-for="item in queryTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
        </el-select>
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="id">
            <el-input v-model="form.blogId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="标题" prop="blogTitle">
            <el-input v-model="form.blogTitle" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="内容" prop="blogBody">
            <el-input v-model="form.blogBody" :rows="3" type="textarea" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="评论数" prop="blogDiscusscount">
            <el-input v-model="form.blogDiscusscount" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="浏览数" prop="blogBlogviews">
            <el-input v-model="form.blogBlogviews" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="发布时间" prop="blogTime">
            <el-date-picker v-model="form.blogTime" type="datetime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="状态" prop="blogState">
            <el-input v-model="form.blogState" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户id" prop="userId">
            <el-input v-model="form.userId" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('blogId')" prop="blogId" label="id" />
        <el-table-column v-if="columns.visible('blogTitle')" prop="blogTitle" label="标题" />
        <el-table-column v-if="columns.visible('blogBody')" prop="blogBody" label="内容" />
        <el-table-column v-if="columns.visible('blogDiscusscount')" prop="blogDiscusscount" label="评论数" />
        <el-table-column v-if="columns.visible('blogBlogviews')" prop="blogBlogviews" label="浏览数" />
        <el-table-column v-if="columns.visible('blogTime')" prop="blogTime" label="发布时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.blogTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('blogState')" prop="blogState" label="状态">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.blogState"
              active-color="#409EFF"
              inactive-color="#F56C6C"
              @change="changeEnabled(scope.row, scope.row.blogState)"
            />
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('userId')" prop="userId" label="用户id" />
        <el-table-column v-permission="['admin','blog:edit','blog:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudBlog from '@/api/blog'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

// crud交由presenter持有
const defaultCrud = CRUD({ title: 'blog', url: 'api/blog', sort: 'blogId,desc', crudMethod: { ...crudBlog }})
const defaultForm = { blogId: null, blogTitle: null, blogBody: null, blogDiscusscount: null, blogBlogviews: null, blogTime: null, blogState: null, userId: null }
export default {
  name: 'Blog',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      permission: {
        add: ['admin', 'blog:add'],
        edit: ['admin', 'blog:edit'],
        del: ['admin', 'blog:del']
      },
      rules: {
        blogTitle: [
          { required: true, message: '标题不能为空', trigger: 'blur' }
        ],
        blogBody: [
          { required: true, message: '内容不能为空', trigger: 'blur' }
        ],
        blogDiscusscount: [
          { required: true, message: '评论数不能为空', trigger: 'blur' }
        ],
        blogBlogviews: [
          { required: true, message: '浏览数不能为空', trigger: 'blur' }
        ],
        blogTime: [
          { required: true, message: '发布时间不能为空', trigger: 'blur' }
        ],
        blogState: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ],
        userId: [
          { required: true, message: '用户id不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'blogId', display_name: 'id' },
        { key: 'blogTitle', display_name: '标题' },
        { key: 'blogBody', display_name: '内容' },
        { key: 'blogDiscusscount', display_name: '评论数' },
        { key: 'blogBlogviews', display_name: '浏览数' },
        { key: 'blogState', display_name: '状态' },
        { key: 'userId', display_name: '用户id' }
      ]
    }
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      const query = this.query
      if (query.type && query.value) {
        this.crud.params[query.type] = query.value
      }
      return true
    },
    // 改变状态
    changeEnabled(data, val) {
      console.log(this.dict)
      this.$confirm('此操作将 "' + this.dict.label.job_status[val] + '" ' + data.name + '岗位, 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crud.crudMethod.edit(data).then(() => {
          crud.notify(this.dict.label.job_status[val] + '成功', 'success')
        }).catch(err => {
          data.enabled = !data.enabled
          console.log(err.data.message)
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    }
  }
}
</script>

<style scoped>

</style>
