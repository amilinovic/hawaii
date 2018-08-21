import { call, put } from 'redux-saga/effects';
import { push } from 'connected-react-router';

import {
  REQUEST_TOKEN_FROM_STORAGE,
  requestTokenFromStorage,
  receiveTokenFromStorageError
} from '../../../store/actions/getTokenFromSessionStorageActions';
import { RECEIVE_TOKEN } from '../../../store/actions/getTokenActions';
import { authenticate } from '../../../store/sagas/getTokenFromSessionStorageSaga';
import { getTokenFromSessionStorage } from '../../../store/services/getTokenFromSessionStorage';

describe('getTokenFromSessionStorageSaga', () => {
  it('should redirect to leave if token exists', () => {
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

    console.log(global.sessionStorage.getItem('token'));
    console.log(global.sessionStorage.getItem('role'));

    const actionType = {
      type: RECEIVE_TOKEN
    };

    const iterator = authenticate();

    // const redirect = sessionStorageMock.getItem ? '/leave' : '/login';
    // console.log(redirect);
    // console.log(getTokenFromSessionStorage)
    expect(iterator.next().value).toEqual(call(getTokenFromSessionStorage));
    expect(iterator.next().value).toEqual(put(actionType));
    expect(iterator.next().value).toEqual(put(push('/leave')));

    expect(iterator.next(global.sessionStorage).value).toEqual(
      put(requestTokenFromStorage(global.sessionStorage.getItem))
    );

    expect(iterator.next().done).toBe(true);
  });
});
