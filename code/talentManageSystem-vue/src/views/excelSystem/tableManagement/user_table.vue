<template>
    <h3>稼働状况（{{beginDate}}~{{endDate}}）</h3>
</template>

<script>
export default{
    data() {
        return {
            date : {
                beginDate: '',
                endDate: '',
            },
            status: 0, //0 —— 不能编辑 ； 1 —— 编辑
            datalist: '',
        }
    },
}
</script>
