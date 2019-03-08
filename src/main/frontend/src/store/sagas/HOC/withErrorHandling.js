import { push } from 'connected-react-router';
import { get } from 'lodash';
import { put } from 'redux-saga/effects';
import { toastrError } from '../toastrHelperSaga';

export function withErrorHandling(saga, errorHandler) {
  return function* sagaWithErrorHandler(...args) {
    try {
      // yield* iterates over a generator and yields every value from it
      // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/yield*
      yield* saga(...args);
    } catch (e) {
      const errorMessage = get(e, 'response.body.error', 'No error message');

      if (e.status === 401) {
        yield put(push('/login'));
      }

      yield errorHandler(errorMessage);
      yield put(toastrError(errorMessage));
    }
  };
}

export const genericErrorHandler = action =>
  function*(e) {
    yield put(action(e));
  };
