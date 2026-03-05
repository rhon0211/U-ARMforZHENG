<template>
    <el-dialog v-model="visible" :title="title" :close-on-click-modal="false" width="550px">
        <el-form label-width="180px" ref="dataForm" :rules="dataRule" :model="dataForm" autocomplete="off">
            <!-- 用户名 -->
            <el-form-item label="ユーザー名" prop="userName" autocomplete="off" v-show="type == 0 || type == 1">
                <el-input style="width: 280px;" v-model="dataForm.userName" clearable autocomplete="off"></el-input>
            </el-form-item>
            <!-- 假名 -->
            <el-form-item label="カタカナ" prop="katakana" v-show="type == 0 || type == 1">
                <el-input style="width: 280px;" v-model="dataForm.katakana" clearable></el-input>
            </el-form-item>
            <!-- 账号 -->
            <el-form-item label="アカウント" prop="account" v-show="type == 0 || type == 1">
                <el-input style="width: 280px;" v-model="dataForm.account" clearable></el-input>
            </el-form-item>
            <!-- 旧密码 -->
            <el-form-item label="以前のパスワード" prop='old_password' v-show="type == 2">
                <el-input style="width: 280px;" v-model="dataForm.old_password" type="password" clearable
                    autocomplete="off"></el-input>
            </el-form-item>
            <!-- 密码 -->
            <el-form-item label="パスワード" prop="password" v-show="type == 0 || type == 2">
                <el-input style="width: 280px;" v-model="dataForm.password" type="password" clearable
                    autocomplete="off"></el-input>
            </el-form-item>
            <!-- 密码（确认用） -->
            <el-form-item label="パスワード（確認用）" prop="password_copy" v-show="type == 0 || type == 2">
                <el-input style="width: 280px;" v-model="dataForm.password_copy" type="password" clearable
                    autocomplete="off"></el-input>
            </el-form-item>
            <!-- 邮箱 -->
            <el-form-item label="メール" prop="email" v-show="type == 0 || type == 1">
                <el-input style="width: 280px;" v-model="dataForm.email" clearable></el-input>
            </el-form-item>
            <!-- 电话号码 -->
            <el-form-item label="電話番号" prop="phoneNumber" v-show="type == 0 || type == 1">
                <el-input style="width: 280px;" v-model="dataForm.phoneNumber" clearable></el-input>
            </el-form-item>
            <!-- 社员编号 -->
            <el-form-item label="社員コード" prop="employeeCode" v-show="type == 0 || type == 1">
                <el-input style="width: 280px;" v-model="dataForm.employeeCode" clearable></el-input>
            </el-form-item>
            <!-- 权限 -->
            <el-form-item label="権限" prop="role" v-show="type == 0 || type == 1">
                <el-select style="width: 280px;" v-model="dataForm.role" clearable>
                    <el-option value="4" label="一般ユーザー"></el-option>
                    <el-option value="5" label="高権限ユーザー"></el-option>
                    <el-option value="6" label="管理者"></el-option>
                </el-select>
            </el-form-item>
            <!-- 活性状态 -->
            <el-form-item label="アクティブ" prop="active" v-show="type == 0 || type == 1">
                <el-select style="width: 280px;" v-model="dataForm.active" clearable>
                    <el-option value="0" label="アクティブ"></el-option>
                    <el-option value="1" label="非アクティブ"></el-option>
                </el-select>
            </el-form-item>
            <!-- 备注 -->
            <el-form-item label="備考" prop="remark" v-show="type == 0 || type == 1">
                <el-input style="width: 280px;" v-model="dataForm.remark" clearable type="textarea"
                    :rows="5"></el-input>
            </el-form-item>
        </el-form>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="visible = false">キャンセル</el-button>
                <el-button @click="dataFormSubmit" type="primary">確認</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
export default {
    data() {
        return {
            visible: false,
            type: 0, //0：新增；1：修改
            dataForm: {
                userId: '',
                userName: '',
                katakana: '',
                account: '',
                old_password: '',
                password: '',
                password_copy: '',
                email: '',
                phoneNumber: '',
                employeeCode: '',
                role: '',
                active: '',
                remark: '',
            },
            dataRule: {
                userName: [
                    { required: true, message: 'ユーザー名を入力してください', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            const pattern = /^[\u4E00-\u9FA5A-Za-z0-9]+$/; // 中文、英文字母、数字
                            if (!value) {
                                callback(new Error('ユーザー名を入力してください'));
                            } else if (!pattern.test(value)) {
                                callback(new Error('特殊文字やスペースを含めないでください'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                katakana: [
                    { required: true, message: 'カタカナを入力してください', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            const pattern = /^[ぁ-んァ-ンー\s]+$/;
                            if (!value) {
                                callback(new Error('カタカナを入力してください'));
                            } else if (!pattern.test(value)) {
                                callback(new Error('カタカナのみ入力してください'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                account: [
                    { required: true, message: 'アカウントを入力してください', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            const pattern = /^[A-Za-z0-9]+$/; // 仅限大写英文字母和数字
                            if (!value) {
                                callback(new Error('アカウントを入力してください'));
                            } else if (!pattern.test(value)) {
                                callback(new Error('英数字を入力してください'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                old_password: [
                    { required: true, message: '以前のパスワードを入力してください', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            const pattern = /^[A-Za-z0-9]+$/; // 仅限英文字母和数字
                            if (!value) {
                                callback(new Error('以前のパスワードを入力してください'));
                            } else if (!pattern.test(value)) {
                                callback(new Error('英数字（大文字のみ）を入力してください'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                password: [
                    { required: true, message: 'パスワードを入力してください', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            const pattern = /^[A-Za-z0-9]+$/; // 仅限英文字母和数字
                            if (!value) {
                                callback(new Error('パスワードを入力してください'));
                            } else if (!pattern.test(value)) {
                                callback(new Error('英数字（大文字のみ）を入力してください'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                password_copy: [
                    { required: true, message: 'パスワードを再入力してください', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            const pattern = /^[A-Za-z0-9]+$/; // 仅限英文字母和数字
                            if (!value) {
                                callback(new Error('パスワードを再入力してください'));
                            } else if (!pattern.test(value)) {
                                callback(new Error('英数字（大文字のみ）を入力してください'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                email: [
                    { required: true, message: 'メールを入力してください', trigger: 'blur' },
                    {
                        type: 'email',
                        message: '有効なメールを入力してください',
                        trigger: 'blur',
                    },
                ],
                employeeCode: [
                    { required: true, message: '社員コードを入力してください', trigger: 'blur' },
                ],
                role: [
                    { required: true, message: '権限を選択してください', trigger: 'change' },
                ],
                active: [
                    { required: true, message: 'アクティブを選択してください', trigger: 'change' },
                ],
            },
        }
    },
    computed: {
        title() {
            let t = ''
            if (this.type == 0) {
                t = '追加'
            } else if (this.type == 1) {
                t = '更新'
            } else if (this.type == 2) {
                t = 'パスワードを更新'
            }
            return t;
        }
    },
    methods: {
        reset() {
            this.dataForm.userId = '';
            this.dataForm.userName = '';
            this.dataForm.katakana = '';
            this.dataForm.account = '';
            this.dataForm.password = '';
            this.dataForm.password_copy = '';
            this.dataForm.old_password = '';
            this.dataForm.email = '';
            this.dataForm.phoneNumber = '';
            this.dataForm.employeeCode = '';
            this.dataForm.role = '';
            this.dataForm.active = '';
            this.dataForm.remark = '';
        },
        init(type, userId) {
            let that = this;
            that.reset();
            that.type = type;

            if (that.type == 0) {
                // 新增
                that.dataForm.userId = '';
            }
            else if (that.type == 1) {
                // 修改
                that.dataForm.userId = userId;
                // 往后端发请求得到原始数据
                that.$httpV2('/user/getUserById', "GET", { userId: userId }, false, function (resp) {
                    let result = resp.result;
                    that.dataForm.userName = result.name || '';
                    that.dataForm.katakana = result.pseudonym || '';
                    that.dataForm.account = result.account || '';
                    that.dataForm.email = result.email || '';
                    that.dataForm.type = result.type || '';
                    that.dataForm.role = result.type || '';
                    that.dataForm.employeeCode = result.code || '';
                    that.dataForm.phoneNumber = result.phone || '';
                    that.dataForm.active = result.status || '';
                    that.dataForm.remark = result.remark || '';
                });
            } else if (that.type == 2) {
                // 修改密码
                that.dataForm.userId = userId;
            }
            that.visible = true;
        },
        dataFormSubmit() {
            let that = this;

            // 新增处理
            if (that.type == 0) {
                // 两次密码是否一致
                if (that.dataForm.password !== that.dataForm.password_copy) {
                    that.$message.error('2回入力したパスワードが一致しません');
                    console.log('Validation errors:', error);
                    return false;
                }
                // 表单项检验
                const fieldsToValidate = ['userName', 'katakana', 'account', 'email', 'employeeCode', 'role', 'active', 'password'];
                that.$refs['dataForm'].validateField(fieldsToValidate, function (valid) {
                    if (!valid) {
                        that.$message.error('情報が正しくありません');
                    } else {
                        let data = {
                            userName: that.dataForm.userName,
                            katakana: that.dataForm.katakana,
                            account: that.dataForm.account,
                            password: that.dataForm.password,
                            email: that.dataForm.email,
                            phoneNumber: that.dataForm.phoneNumber,
                            employeeCode: that.dataForm.employeeCode,
                            role: that.dataForm.role,
                            active: that.dataForm.active,
                            remark: that.dataForm.remark
                        };
                        that.$httpV2('/user/add', 'POST', data, false, function (resp) {
                            that.visible = false;
                            that.$emit("refreshDataList");
                        });
                    }
                });
            }
            // 更新处理
            else if (that.type == 1) {
                let url = '/user/update';
                let method = 'PUT';
                const fieldsToValidate = ['userName', 'katakana', 'account', 'mail', 'employeeCode', 'role', 'active'];
                that.$refs['dataForm'].validateField(fieldsToValidate, function (valid) {
                    if (!valid) {
                        that.$message.error('情報が正しくありません');
                    } else {
                        let data = {
                            userId: that.dataForm.userId,
                            userName: that.dataForm.userName,
                            katakana: that.dataForm.katakana,
                            account: that.dataForm.account,
                            email: that.dataForm.email,
                            phoneNumber: that.dataForm.phoneNumber,
                            employeeCode: that.dataForm.employeeCode,
                            role: that.dataForm.role,
                            active: that.dataForm.active,
                            remark: that.dataForm.remark
                        };
                        that.$httpV2('/user/update', 'PUT', data, false, function (resp) {
                            that.visible = false;
                            that.$emit("refreshDataList");
                        });
                    }
                });
            }
            // 修改密码处理
            else if (that.type == 2) {
                // 两回密码是否一致
                console.log('旧--', that.dataForm.password);
                console.log('旧-确认-', that.dataForm.password_copy);

                if (that.dataForm.password !== that.dataForm.password_copy) {
                    that.$message.error('2回入力したパスワードが一致しません');
                    return false;
                }
                let old_password_data = {
                    userId: that.dataForm.userId,
                    password: that.dataForm.old_password,
                }
                // 旧密码是否正确
                that.$httpV2('/user/verify-password', 'GET', old_password_data, false, function (resp) {
                    if (resp.result == false) {
                        // 不正确
                        that.$message.error('以前のパスワードが正しくありません');
                        return false;
                    } else if (resp.result == true) {
                        // 正确，先检验密码是否符合规则，再修改密码
                        const fieldsToValidate = ['password'];
                        that.$refs['dataForm'].validateField(fieldsToValidate, function (valid) {
                            if (!valid) {
                                that.$message.error('情報が正しくありません');
                            } else {
                                // 修改密码
                                let new_password_data = {
                                    userId: that.dataForm.userId,
                                    password: that.dataForm.password,
                                }
                                that.$httpV2('/user/updatePassword', 'PUT', new_password_data, false, function (resp) { });
                            }
                        });
                    }
                })
            }
        }
    }
}
</script>