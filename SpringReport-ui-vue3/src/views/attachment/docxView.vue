<template>
    <div class="docx-container">
      <div ref="file"></div>
    </div>
</template>

<script>
import axios from "axios";
import { renderAsync } from "docx-preview";

export default {
  props: {
    url: {
      type: String,
      default: "",
    },
  },

  mounted() {
    this.renderFile();
  },


  methods: {
    renderFile() {
      axios({
        method: "get",
        responseType: "blob",
        url: this.url,
      }).then((response) => {
        renderAsync(response.data, this.$refs.file);
      });
    },
  },
};
</script>



