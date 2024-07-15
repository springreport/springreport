import { setting } from '@/config/setting';
const { tokenTableName } = setting;
import Cookies from 'js-cookie';
export function getAccessToken() {
  return Cookies.get(tokenTableName);
}

export function setAccessToken(accessToken) {
  return Cookies.set(tokenTableName, accessToken);
}

export function removeAccessToken() {
  return Cookies.remove(tokenTableName);
}
