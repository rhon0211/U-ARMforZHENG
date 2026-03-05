<template>
  <div class="page">
    <el-row type="flex" justify="center" align="middle" class="container">
      <el-col :lg="14" :xl="10">
        <el-row class="panel">
          <el-col :span="24">
            <div class="right">
              <div class="title-container">
                <h2>営業売上管理　U-ARM</h2>
                &nbsp;&nbsp;&nbsp;&nbsp;
              </div>
              <div class="row">
                <el-input v-model="email" placeholder="メール" size="large" clearable>
                  <template v-slot:append>
                    <el-button :disabled="isDisabled" @click="getVerificationCode" type="primary">
                      {{ buttonText }}
                    </el-button>
                  </template>
                </el-input>
                <div v-if="emailError" class="error-text">{{ emailError }}</div>
              </div>
              <div class="row">
                <el-input v-model="password" placeholder="パスワード" type="password" size="large" show-password clearable
                  @blur="validatePassword"></el-input>
                <div v-if="passwordError" class="error-text">{{ passwordError }}</div>
              </div>
              <div class="row">
                <el-input v-model="code" placeholder="確認コード" size="large" clearable></el-input>
                <div v-if="codeError" class="error-text">{{ codeError }}</div>
              </div>
              <!-- <div class="row">
                <el-radio-group v-model="choose">
                  <el-radio :label="0">人材管理</el-radio>
                  <el-radio :label="1">フォーム管理</el-radio>
                </el-radio-group>
              </div> -->
              <div class="row">
                <el-button type="primary" class="btn" size="large" @click="login">
                  システムログイン
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: "",
      password: "",
      code: "",
      lang: "zh-CN",
      isDisabled: false,
      countdown: 0,
      timer: null,
      choose: 1,
      emailError: "",
      passwordError: "",
      codeError: "",
    };
  },
  computed: {
    buttonText() {
      return this.countdown > 0 ? `${this.countdown}秒` : "取得確認コード";
    },
  },
  methods: {
    getVerificationCode() {
      let that = this;
      if (!this.email) {
        that.emailError = "メールを入力してください";
        return;
      } else {
        that.emailError = "";
      }
      let data = {
        email: that.email,
        systemCode: that.choose,
      };
      that.$http("/user/emailCode", "POST", data, true, function (resp) {
        if (resp.code === 200) {
          that.$message.success("確認コードがメールに送信されました");
          that.isDisabled = true;
          that.countdown = 60;
          that.timer = setInterval(() => {
            if (that.countdown > 0) {
              that.countdown--;
            } else {
              that.isDisabled = false;
              clearInterval(that.timer);
            }
          }, 1000);
        }
      });
    },
    validatePassword() {
      if (!this.password || this.password.length < 8) {
        this.passwordError = "パスワードは8文字以上である必要があります";
      } else {
        this.passwordError = "";
      }
    },
    login() {
      let that = this;
      let valid = true;
      if (!this.email) {
        that.emailError = "メールを入力してください";
        valid = false;
      }
      if (!this.password || this.password.length < 8) {
        that.passwordError = "パスワードは8文字以上である必要があります";
        valid = false;
      }
      if (!this.code) {
        that.codeError = "認証コードを入力してください";
        valid = false;
      }
      if (!valid) return;

      let data = {
        email: that.email,
        password: btoa(that.password),//base64加密
        code: that.code,
        lang: that.lang,
        systemCode: that.choose
        // password: '12345678',
      };
      that.$http("/login", "POST", data, true, function (resp) {
        if (resp.code === 200) {
          localStorage.setItem("token", resp.token);
          localStorage.setItem("permissions", JSON.stringify(resp.permissions));
          that.$message.success("ログイン成功");
          that.$router.push({ name: "BlankPage" });
        } else {
          that.$message.error(`ログインに失敗しました: ${resp.msg}`);
        }
      });
    },
  },
};
</script>

<style lang="less" scoped="scoped">
@import url("login.less");

.error-text {
  color: red;
  font-size: 12px;
  margin-top: 4px;
}
</style>
