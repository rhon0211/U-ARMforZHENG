<template>
  <el-dialog
      :title="!dataForm.technicianId ? '追加' : '変更'"
      :close-on-click-modal="false"
      v-model="visible"
      width="450px"
  >
    <el-form :model="dataForm" ref="dataForm" :rules="dataRule" label-width="150px">
      <el-form-item label=" " prop="picture">
        <el-upload
            :action="action"
            :headers="{ token: token }"
            :with-credentials="true"
            :on-success="updatePhotoSuccess"
            :show-file-list="false"
        >
          <el-image
              style="width: 120px; height: 153px; border: 2px solid #D3EDFA; border-radius: 5px;"
              :src="dataForm.picture"
              fit="cover"
          >
            <template #error>
              <div class="error-img">
                <el-icon>
                  <Picture />
                </el-icon>
              </div>
            </template>
          </el-image>
        </el-upload>
      </el-form-item>
      <el-form-item label="技術者氏名" prop="technicianName">
        <el-input v-model="dataForm.technicianName" style="width:100%" clearable />
      </el-form-item>
      <el-form-item label="カタカナ：" prop="katakana">
        <el-input v-model="dataForm.katakana" clearable/>
      </el-form-item>
      <el-form-item label="ローマ字：" prop="roman">
        <el-input v-model="dataForm.roman" clearable/>
      </el-form-item>
      <el-form-item label="生年月日：" prop="birthday">
        <el-date-picker
            v-model="dataForm.birthday"
            type="date"
            placeholder="生年月日を選択"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            clearable
            style="width: 100%"
        />
      </el-form-item>      <el-form-item label="所属（会社名）：" prop="belongCompany">
        <el-input v-model="dataForm.belongCompany" clearable/>
      </el-form-item>
      <el-form-item label="営業担当者：" prop="representative">
        <el-input v-model="dataForm.representative" clearable/>
      </el-form-item>
      <el-form-item label="備考：" prop="remark">
        <el-input type="textarea" rows="1" v-model="dataForm.remark" clearable/>
      </el-form-item>
    </el-form>
    <template #footer>
            <span class="dialog-footer">
                <el-button @click="visible = false">キャンセル</el-button>
                <el-button type="primary" @click="dataFormSubmit">確定</el-button>
            </span>
    </template>
  </el-dialog>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  data: function() {
    return {
      token: localStorage.getItem("token"),
      action: `${this.$baseUrl}/file/upload`,

      visible: false,
      typeList: [],
      dataForm: {
        technicianId: null,
        technicianName: null,
        picture: null,
        katakana: null,
        roman: null,
        birthday: null,
        belongCompany: null,
        representative: null,
        remark: null,
      },
      dataRule: {
        technicianName: [],
      }
    };
  },

  methods: {
    init: function(technicianInfo) {
      let that = this;
      that.setFormRules();
      that.reset();
      that.$nextTick(() => {
        that.$refs["dataForm"].resetFields();
        if (technicianInfo != null && technicianInfo != "") {
          that.dataForm = {
            ...JSON.parse(JSON.stringify(technicianInfo)),
          };
          console.log(this.dataForm);
        }
      })
      that.visible = true;
    },
    reset: function() {
      let dataForm = {
        technicianId: null,
        technicianName: null,
        picture: null,
        katakana: null,
        roman: null,
        birthday: null,
        belongCompany: null,
        representative: null,
        remark: null,
      };
      this.dataForm = dataForm;
    },
    setFormRules: function() {
      this.dataRule.technicianName = [
        { required: true, message: "技術者名の入力をお願いします" }
      ];
    },
    updatePhotoSuccess: function (resp) {
      this.dataForm.picture = resp.file.url;
    },
    dataFormSubmit: function() {
      let that = this;
      this.$refs['dataForm'].validate(valid => {
        const operation = !that.dataForm.technicianId ? 'insert' : 'update';
        const method = operation === 'insert' ? 'POST' : 'PUT';
        let data = {
          technicianId: this.dataForm.technicianId = null ? "" : this.dataForm.technicianId,
          name: this.dataForm.technicianName = null ? "" : this.dataForm.technicianName,
          picture: this.dataForm.picture = null ? "" : this.dataForm.picture,
          katakana: this.dataForm.katakana = null ? "" : this.dataForm.katakana,
          roman: this.dataForm.roman = null ? "" : this.dataForm.roman,
          birthday: this.dataForm.birthday = null ? "" : this.dataForm.birthday,
          belongCompany: this.dataForm.belongCompany = null ? "" : this.dataForm.belongCompany,
          representative: this.dataForm.representative = null ? "" : this.dataForm.representative,
          remark: this.dataForm.remark = null ? "" : this.dataForm.remark,
        }
        if (valid) {
          that.$httpV2(`/technician`, method, data, true, function(resp) {
                ElMessage({
                  message: '操作が正常に完了しました',
                  type: 'success'
                });
                that.visible = false;
                that.$emit('refreshDataList');
              }
          );
        }
      });
    },
  },
}
</script>

<style lang="less" scoped="scoped"></style>