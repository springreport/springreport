<template>
  <div class="report-chart">
    <div class="chart-item" v-for="(options, index) in info" :key="index" v-show="info && info.length>0">
      <!-- <EchartMap
        :id="`chartMap${index}`"
        :options="options"
        height="358px"
        v-if="options.map && options.map.max != null"
      /> -->
      <Echart :id="`chart${index}`" :options="options" height="358px"/>
    </div>
    <div class="chart-item" v-show="!info || info.length == 0">
      <van-empty description="暂无数据" />
    </div>
  </div>
</template>

<script>
import Echart from "@/components/echart/index.vue";
// import EchartMap from "@/components/echart/map.vue";
import { allThemeMap } from '@visactor/vchart-theme';
import veODesignLight from '@visactor/vchart-theme/public/veODesignLight.json';
import veODesignLightFinance from '@visactor/vchart-theme/public/veODesignLightFinance.json';
import veODesignLightGovernment from '@visactor/vchart-theme/public/veODesignLightGovernment.json';
import veODesignLightConsumer from '@visactor/vchart-theme/public/veODesignLightConsumer.json';
import veODesignLightAutomobile from '@visactor/vchart-theme/public/veODesignLightAutomobile.json';
import veODesignLightCulturalTourism from '@visactor/vchart-theme/public/veODesignLightCulturalTourism.json';
import veODesignLightMedical from '@visactor/vchart-theme/public/veODesignLightMedical.json';
import veODesignLightNewEnergy from '@visactor/vchart-theme/public/veODesignLightNewEnergy.json';
import veODesignDark from '@visactor/vchart-theme/public/veODesignDark.json';
import veODesignDarkFinance from '@visactor/vchart-theme/public/veODesignDarkFinance.json';
import veODesignDarkGovernment from '@visactor/vchart-theme/public/veODesignDarkGovernment.json';
import veODesignDarkConsumer from '@visactor/vchart-theme/public/veODesignDarkConsumer.json';
import veODesignDarkAutomobile from '@visactor/vchart-theme/public/veODesignDarkAutomobile.json';
import veODesignDarkCulturalTourism from '@visactor/vchart-theme/public/veODesignDarkCulturalTourism.json';
import veODesignDarkMedical from '@visactor/vchart-theme/public/veODesignDarkMedical.json';
import veODesignDarkNewEnergy from '@visactor/vchart-theme/public/veODesignDarkNewEnergy.json';
import VChart from '@visactor/vchart';
export default {
  name: "ReportChart",
  data() {
    return {};
  },
  props: {
    info: {
      type: Array,
      default: () => [],
    },
  },
  components: {
    Echart,
    // EchartMap,
  },
  watch: {},
  mounted() {
     allThemeMap.forEach((theme, name) => {
        VChart.ThemeManager.registerTheme(name, theme);
    });
    VChart.ThemeManager.registerTheme('veODesignLight', veODesignLight);
    VChart.ThemeManager.registerTheme('veODesignLightFinance', veODesignLightFinance);
    VChart.ThemeManager.registerTheme('veODesignLightGovernment', veODesignLightGovernment);
    VChart.ThemeManager.registerTheme('veODesignLightConsumer', veODesignLightConsumer);
    VChart.ThemeManager.registerTheme('veODesignLightAutomobile', veODesignLightAutomobile);
    VChart.ThemeManager.registerTheme('veODesignLightCulturalTourism', veODesignLightCulturalTourism);
    VChart.ThemeManager.registerTheme('veODesignLightMedical', veODesignLightMedical);
    VChart.ThemeManager.registerTheme('veODesignLightNewEnergy', veODesignLightNewEnergy);
    VChart.ThemeManager.registerTheme('veODesignDark', veODesignDark);
    VChart.ThemeManager.registerTheme('veODesignDarkFinance', veODesignDarkFinance);
    VChart.ThemeManager.registerTheme('veODesignDarkGovernment', veODesignDarkGovernment);
    VChart.ThemeManager.registerTheme('veODesignDarkConsumer', veODesignDarkConsumer);
    VChart.ThemeManager.registerTheme('veODesignDarkAutomobile', veODesignDarkAutomobile);
    VChart.ThemeManager.registerTheme('veODesignDarkCulturalTourism', veODesignDarkCulturalTourism);
    VChart.ThemeManager.registerTheme('veODesignDarkMedical', veODesignDarkMedical);
    VChart.ThemeManager.registerTheme('veODesignDarkNewEnergy', veODesignDarkNewEnergy);
  },
  methods: {},
};
</script>

<style scoped lang="scss"></style>
