import { call, put } from 'redux-saga/effects';

import {
  REQUEST_TOKEN_FROM_STORAGE,
  requestTokenFromStorage
} from '../../../store/actions/getTokenFromSessionStorageActions';
import { authenticate } from '../../../store/sagas/getTokenFromSessionStorageSaga';
import { getTokenFromSessionStorage } from '../../../store/services/getTokenFromSessionStorage';

describe('getTokenAndRoleFromSessionStorageSaga', () => {
  it('should get token if token exists', () => {
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

    const iterator = authenticate();

    expect(iterator.next().value).toEqual(call(getTokenFromSessionStorage));

    expect(iterator.next(global.sessionStorage).value).toEqual(
      put(requestTokenFromStorage(global.sessionStorage))
    );

    expect(iterator.next().done).toBe(true);
  });

  it('should get role if role exists', () => {
    const SessionStorageMock = {
      getItem(key) {
        return this[key] || null;
      },

      setItem(key, value) {
        this[key] = value;
      }
    };

    global.sessionStorage = Object.assign({}, SessionStorageMock);

    global.sessionStorage.setItem('role', 'testRole');

    const iterator = authenticate();

    expect(iterator.next().value).toEqual(call(getTokenFromSessionStorage));

    expect(iterator.next(global.sessionStorage.role).value).toEqual(
      put(requestTokenFromStorage(global.sessionStorage.role))
    );

    expect(iterator.next().done).toBe(true);
  });
});
