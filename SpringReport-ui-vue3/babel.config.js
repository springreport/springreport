module.exports = {
  presets: ['@vue/cli-plugin-babel/preset'],
  plugins: [
    [
      'component',
      {
        libraryName: 'element-plus',
        styleLibraryName: 'theme-chalk',
      },
      'import',
      {
        libraryName: '@icon-park/vue-next',
        libraryDirectory: 'es/icons',
        camel2DashComponentName: false,
      },
    ],
  ],
};
