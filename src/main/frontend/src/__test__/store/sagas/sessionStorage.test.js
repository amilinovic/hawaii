import {
  REQUEST_TOKEN_FROM_STORAGE,
  requestTokenFromStorage
} from '../../../store/actions/getTokenFromSessionStorageActions';
import { getTokenFromSessionStorage } from '../../../store/services/getTokenFromSessionStorage';

describe('getTokenAndRoleFromSessionStorageSaga', () => {
  it('should get token and role if token and role exists', () => {
    const SessionStorageMock = {
      getItem(key) {
        return this[key] || null;
      },

      setItem(key, value) {
        this[key] = value;
      }
    };

    global.sessionStorage = Object.assign({}, SessionStorageMock);

    global.sessionStorage.setItem('token', '123456');
    global.sessionStorage.setItem('role', 'testRole');

    expect(getTokenFromSessionStorage()).toEqual({
      token: global.sessionStorage.token,
      role: global.sessionStorage.role
    });
  });
});
