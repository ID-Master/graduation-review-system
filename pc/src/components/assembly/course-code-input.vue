<template>
    <div class="course-code-input">
        <el-form ref="form" :model="codeForm" label-position="right" size="small">
            <el-row>
                <el-col :span="10" style="margin-right: 4px;">
                    <el-form-item prop="letterCode" :rules="[
                        { validator: letterCodeValid, trigger: 'blur' }
                    ]">
                        <el-input v-model="codeForm.letterCode" placeholder="Please input" :maxlenght="3" @input="handleForm"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item prop="numberCode" :rules="[
                        { validator: numberCodeValid, trigger: 'blur' }
                    ]">
                        <el-input v-model="codeForm.numberCode" placeholder="Please input" :maxlenght="4" @input="handleForm"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
    </div>
</template>
  
<script>
export default {
    props: {
        value: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            visible: false,
            codeForm: {
                /** 字段编号 - 3位 */
                letterCode: '',
                /** 数字编号 - 4位 */
                numberCode: '',
            },
        }
    },
    watch: {
        value: {
            immediate: true,
            handler(val) {
                if (val) {
                    this.codeForm.letterCode = val.substring(0, 3);
                    this.codeForm.numberCode = val.substring(3);
                } else {
                    this.codeForm.letterCode = '';
                    this.codeForm.numberCode = '';
                }
            }
        },
    },
    methods: {
        letterCodeValid(rule, value, callback) {
            if (!value) {
                callback(new Error('Please input'));
            } else if (value.length !== 3 || !/^[A-Z]+$/.test(value)) {
                callback(new Error('Please enter a 3-digit uppercase letter'));
            } else {
                callback();
            }
        },
        numberCodeValid(rule, value, callback) {
            if (!value) {
                callback(new Error('Please input'));
            } else if (value.length !== 4 || !/^[0-9]+$/.test(value)) {
                callback(new Error('Please enter a 4-digit number'));
            } else {
                callback();
            }
        },
        handleForm() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    this.$emit('input', `${this.codeForm.letterCode}${this.codeForm.numberCode}`);
                }
            });
        },
    },
    mounted() { },
}
</script>
  
<style scoped>
.l-btns {
    text-align: right;
}
</style>
  