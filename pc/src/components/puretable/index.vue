<template>
  <div class="wrap">
    <table class="hovertable">
      <thead>
        <tr name="calc" class="calc" desc="tr">
          <th v-for="(col, index) in trData" :key="index" v-html="col.text" />
        </tr>
      </thead>
      <tbody v-if="row">
        <tr v-for="(item, index) in rowData" :key="index" name="calc" class="calc" desc="tr">
          <td v-for="(it, index) in trData" :key="index">
            {{ item[it.type] }}
          </td>
        </tr>
      </tbody>
      <tbody v-else>
        <tr v-for="(item, index) in tdData" :key="index" name="calc" class="calc" desc="tr">
          <td v-for="(it, index) in trData" :key="index">
            <span v-if="it.type == 'coureCode' && item[it.type].indexOf('MAJOR_') != -1"></span>
            <span v-else-if="it.type == 'coureCode' && item[it.type].indexOf('FREE_') != -1"></span>
            <span v-else>{{ item[it.type] }}</span>
          </td>
        </tr>
      </tbody>
      <tfoot name="calc" class="calc" desc="tfoot">
        <slot name="thBottom" />
      </tfoot>
    </table>
    <slot />
  </div>
</template>
<script>
export default {
  name: 'puretable',
  props: {
    tdData: {
      type: Array,
      default: [],
    },
    trData: {
      type: Array,
      default: [],
    },
    row: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      rowData: [],
    }
  },
  created() {
    this.addRowData()
    console.log(this.tdData, 52)
  },
  methods: {
    addRowData() {
      if (this.row) {
        this.tdData.forEach((item) => {
          this.rowData.push(item)
        })
        for (let i = 1; i <= this.row; i++) {
          this.rowData.push({
            coureCode: '',
            title: '',
            Units: '',
            selfCheck: '',
            Remark: '',
            Minor: '',
          })
        }
      }
    },
  },
}
</script>
<style scoped>
table {
  width: 100%;
  border-spacing: 0;
}

td,
th {
  padding: 0;
}

table.hovertable {
  text-align: center;
  width: 100%;
  font-family: verdana, arial, sans-serif;
  font-size: 11px;
  color: #333333;
}

table.hovertable th {
  background-color: #6a1d72;
  border-width: 1px;
  padding: 8px;
  border-right: 1px solid rgb(201, 201, 201);
  align-items: center;
  justify-content: center;
  text-align: left;
  color: #fff;
}
table.hovertable thead tr {
  border: 1px solid rgb(201, 201, 201);
}
table.hovertable tbody tr {
  border: 1px solid rgb(201, 201, 201);
}
table.hovertable td {
  border-width: 1px;
  padding: 10px;
  border: 1px solid rgb(201 201 201);
  align-items: center;
  justify-content: center;
  text-align: left;
  min-height: 20px;
}
</style>