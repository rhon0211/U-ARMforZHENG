import { createI18n } from 'vue-i18n';

// 动态导入语言文件
async function loadLocaleMessages() {
  const contexts = import.meta.globEager('./*.js');
  const messages = {};
  for (const path in contexts) {
    const locale = path.replace('./', '').replace('.js', '');
    messages[locale] = contexts[path].default;
  }
  return messages;
}


const i18n = createI18n({
  locale: localStorage.getItem('lang'),
  messages: await loadLocaleMessages()
});

export default i18n;


