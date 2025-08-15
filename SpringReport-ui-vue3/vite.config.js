import { defineConfig } from 'vite';
const path = require('path');
import vue from '@vitejs/plugin-vue';
import legacy from '@vitejs/plugin-legacy';
import { viteMockServe } from 'vite-plugin-mock';
import { setting } from './src/config/setting';
import { svgBuilder } from './src/plugin/svgBuilder';

import OptimizationPersist from 'vite-plugin-optimize-persist';
import PkgConfig from 'vite-plugin-package-config';
import vueI18n from '@intlify/vite-plugin-vue-i18n';

import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import Icons from 'unplugin-icons/vite';
import IconsResolver from 'unplugin-icons/resolver';
import compressPlugin from 'vite-plugin-compression';

const {
  base,
  publicDir,
  outDir,
  assetsDir,
  sourcemap,
  cssCodeSplit,
  host,
  port,
  strictPort,
  open,
  cors,
  brotliSize,
  logLevel,
  clearScreen,
  drop_console,
  drop_debugger,
  chunkSizeWarningLimit,
} = setting;

const isDev = process.env.NODE_ENV === 'development';

const loadI18n = isDev ? vueI18n({ include: path.resolve(__dirname, './src/locales/**') }) : '';
// https://vitejs.dev/config/
export default defineConfig({
  root: process.cwd(),
  base: '/SpringReport-vue3',
  publicDir,
  logLevel,
  clearScreen,
  plugins: [
    vue(),
    PkgConfig(),
    OptimizationPersist(),
    loadI18n,
    legacy({
      polyfills: ['es.promise.finally', 'es/map', 'es/set'],
      modernPolyfills: ['es.promise.finally'],
    }),
    AutoImport({
      // 自动导入 Vue 相关函数，如：ref, reactive, toRef 等
      imports: ['vue'],
      resolvers: [
        ElementPlusResolver(),
        // 自动导入图标组件
        IconsResolver({
          prefix: 'Icon',
        }),
      ],
    }),
    Components({
      resolvers: [
        ElementPlusResolver({
          importStyle: 'sass',
          // directives: true,
          // version: "2.1.5",
        }),
        // 自动注册图标组件
        IconsResolver({
          enabledCollections: ['ep'],
        }),
      ],
    }),
    Icons({
      autoInstall: true,
    }),
    viteMockServe({
      mockPath: 'mock',
      supportTs: false,
      localEnabled: isDev,
      prodEnabled: !isDev,
      // injectCode: `
      //     import { setupProdMockServer } from './mockProdServer';
      //     setupProdMockServer();
      //   `,
    }),
    compressPlugin({
      ext: '.gz',
      deleteOriginFile: false, // 是否删除原始文件
    }),
    svgBuilder('./src/icons/svg/'),
  ],

  server: {
    host,
    port,
    cors,
    strictPort,
    open,
    fs: {
      strict: false,
    },
    proxy: {
      '/SpringReport/api': {
        target: 'http://localhost:9099/springReport',
        changeOrigin: true,
        ws: true,
        rewrite: (path) => path.replace(/^\/SpringReport/, ''),
      },
      '/geoJson': {
        target: 'https://www.springreport.vip/',
        changeOrigin: true,
        pathRewrite: {
          '^/geoJson': '/geoJson',
        },
      },
    },
  },

  resolve: {
    // 设置别名
    alias: {
      views: path.resolve(__dirname, 'src/views'),
      styles: path.resolve(__dirname, 'src/styles'),
      '@': path.resolve(__dirname, 'src'),
    },
  },

  css: {
    preprocessorOptions: {
      // 引入公用的样式
      scss: {
        additionalData: `@use "@/styles/index.scss" as *; @use "@/styles/element/index.scss" as *;`,
        charset: false,
      },
    },
  },

  corePlugins: {
    preflight: false,
  },

  build: {
    target: 'es2015',
    outDir,
    assetsDir,
    sourcemap,
    cssCodeSplit,
    brotliSize,
    // rollupOptions: {
    //   output: {
    //     // chunkFileNames: 'static/js/[name]-[hash].js',
    //     // entryFileNames: 'static/js/[name]-[hash].js',
    //     // assetFileNames: 'static/[ext]/[name]-[hash].[ext]',
    //   },
    // },
    terserOptions: {
      compress: {
        keep_infinity: true,
        drop_console,
        drop_debugger,
      },
    },
    chunkSizeWarningLimit,
  },

  optimizeDeps: {
    // 检测需要预构建的依赖项
    include: [],
  },
});
