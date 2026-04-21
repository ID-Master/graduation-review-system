<template>
  <div>
    <div v-if="textData.title" name="calc" class="calc stDfou">{{ textData.title }}</div>
    <div v-if="textData.content" name="calc" class="calc" v-html="textData.content"></div>
  </div>
</template>
<script>
export default {
  props: {
    txData: {
      type: Array,
      default: [],
    },
  },
  computed: {
    textData() {
      const datas = this.txData.filter(item => item.categoryCode === 'Free Elective');
      return datas.length > 0 ? datas[0] : {};
    },
  },
}
</script>
<style scoped>
.stDfou {
  padding: 10px 0;
  font-weight: 600;
}
p {
  padding: 6px;
  margin: 0;
}
</style>
