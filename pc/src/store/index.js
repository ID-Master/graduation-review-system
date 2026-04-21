import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    templateField: {
      index: 0,
      field: '',
    },
    passedNumField: '',
    isExceedTotalScore: false,
    /** 课程互斥，第五步，A、B有相同课程，只能有一个被选中 */
    partHaveValA: [],
    partHaveValB: [],
  },
  mutations: {
    SET_TEMPLATE_FIELD (state, templateField) {
      state.templateField = templateField;
      state.passedNumField = '';
    },
    SET_PASSED_NUM_FIELD (state, field) {
      state.passedNumField = field;
      state.templateField.field = '';
      state.templateField.index = 0;
    },
    SET_EXCEED_TOTAL_SCORE (state, flag) {
      state.isExceedTotalScore = flag;
    },
    SET_CLEAR(state) {
      state.isExceedTotalScore = false;
      state.templateField.index = 0;
      state.templateField.field = '';
      state.passedNumField = '';
    },
    INIT_PART_HAVE_VAL(state, data) {
      state[data.attribute] = data.codes;
    },
    SET_PART_HAVE_VAL(state, data) {
      let codes = state[data.attribute];

      if (data.type === 'push' && !codes.includes(data.code)) {
        codes.push(data.code);
      }

      if (data.type === 'reduce' && codes.includes(data.code)) {
        codes = codes.filter(code => code !== data.code);
      }

      state[data.attribute] = codes;
    },
  },
  actions: {
    setPartHaveVal({ commit }, data) {
      

      commit('SET_PART_HAVE_VAL', data);
    },
    initPartHaveVal({ commit }, data) {
      commit('INIT_PART_HAVE_VAL', data);
    },
    setTemplateField({ commit }, templateField) {
      commit('SET_TEMPLATE_FIELD', templateField);
    },
    setPassedNumField({ commit }, field) {
      commit('SET_PASSED_NUM_FIELD', field);
    },
    setExceedTotalScore({ commit }, flag) {
      commit('SET_EXCEED_TOTAL_SCORE', flag);
    },
    clear({ commit }) {
      console.log('=2==2=2=2==clearclearclearclear');
      commit('SET_CLEAR');
    },
  },
})

export default store;