<!-- src/views/testFan/index.vue -->
<template>
  <div class="test-fan">
    <card-list></card-list>
  </div>
</template>

<script>
import cardList from "../component/screen/draggablecomponent/cardList.vue";

export default {
  name: "TestFan",
  components: {
    cardList,
  },
  data() {
    return {
    };
  },
  methods: {
    
  },
};
</script>

<style lang="scss" scoped>
.test-fan {
  padding: 20px;
}

.example {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 4px;

  h3 {
    margin-top: 0;
    color: #fff;
  }
}

button {
  padding: 8px 16px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #337ecc;
  }
}
</style>