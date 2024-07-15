import { setting } from '@/config/setting';
const { langKey, themeKey } = setting;
import Cookies from 'js-cookie';

export function getLanguage() {
  return Cookies.get(langKey);
}

export function setLanguage(lang) {
  return Cookies.set(langKey, lang);
}

export function getSettings() {
  const settings = Cookies.get(themeKey);
  return settings ? JSON.parse(settings) : null;
}

export function setSettings(theme) {
  return Cookies.set(themeKey, JSON.stringify(theme));
}
