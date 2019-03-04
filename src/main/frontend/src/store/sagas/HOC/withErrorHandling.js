import { push } from 'connected-react-router';
import { actions as toastrActions } from 'react-redux-toastr';
import { put } from 'redux-saga/effects';

export function withErrorHandling(saga, errorHandler) {
  return function* sagaWithErrorHandler(...args) {
    try {
      // yield* iterates over a generator and yields every value from it
      // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/yield*
      yield* saga(...args);
    } catch (e) {
      if (e.status === 401) {
        yield put(push('/login'));
      }
      yield errorHandler();
      yield put(
        toastrActions.add({
          type: 'error',
          title: 'Error',
          message: e.message
        })
      );
    }
  };
}

export const errorHandlingAction = function*(action) {
  yield put(action());
};
