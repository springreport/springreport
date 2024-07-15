const modulesFiles = import.meta.globEager('./*/*.vue');

// 注册
export default (app) => {
  for (const path in modulesFiles) {
    const componentName = modulesFiles[path].default.name;
    app.component(componentName, modulesFiles[path].default);
  }
};
