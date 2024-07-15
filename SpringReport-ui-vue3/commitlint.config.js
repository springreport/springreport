module.exports = {
  ignores: [(commit) => commit.includes('init')],
  extends: ['@commitlint/config-conventional'],
  rules: {
    'body-leading-blank': [2, 'always'],
    'footer-leading-blank': [1, 'always'],
    'header-max-length': [2, 'always', 108],
    'subject-empty': [2, 'never'],
    'type-empty': [2, 'never'],
    'type-enum': [
      2,
      'always',
      [
        'feat', // 增加新功能
        'fix', // 修复问题/BUG
        'perf', // 优化/性能提升
        'style', //  代码风格相关无影响运行结果的
        'docs', // 文档/注释
        'test', // 测试相关
        'refactor', // 重构
        'build', // 生产
        'ci', // 持续集成
        'chore', // 依赖更新/脚手架配置修改等
        'revert', // 撤销修改
        'wip', // 开发中
        'workflow', // 工作流改进
        'types', // 类型修改
        'release', // 发布正式版
      ],
    ],
  },
};
