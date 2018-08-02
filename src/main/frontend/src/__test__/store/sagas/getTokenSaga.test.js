import { call, put } from 'redux-saga/effects';

import {
  receiveToken,
  receiveTokenError
} from '../../../store/actions/GetTokenActions';
import { getToken } from '../../../store/sagas/getTokenSaga';
import { tokenRequest } from '../../../store/services/getTokenRequest';

describe('Get Token saga', () => {
  it('should get token', () => {
    const accessToken = {
      token: '2d153236-d103-4fa0-a3cb-8f26a14c1c45',
      role: 'HR_MANAGER'
    };
    const googleToken = 'testGoogleToken';

    const iterator = getToken({
      payload: {
        accessToken
      }
    });

    expect(iterator.next().value).toEqual(call(tokenRequest, accessToken));

    expect(iterator.next(googleToken).value).toEqual(
      put(receiveToken(googleToken))
    );

    expect(iterator.next().done).toBe(true);
  });

  it('should handle get token error', () => {
    const iterator = getToken({
      payload: {}
    });
    const err = new Error('error');

    iterator.next();
    expect(iterator.throw(err).value).toEqual(put(receiveTokenError(err)));
  });
});
