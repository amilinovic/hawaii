import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingTeam,
  errorRemovingTeam,
  receiveTeam,
  removeTeam,
  removeTeamSuccess,
  requestTeam,
  updateTeam,
  updateTeamError,
  updateTeamSuccessful
} from '../actions/teamActions';
import {
  getTeamApi,
  removeTeamApi,
  updateTeamApi
} from '../services/teamService';
import {
  errorHandlingAction,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getTeamSaga = function*(action) {
  const teamInformation = yield call(getTeamApi(action.payload));
  yield put(receiveTeam(teamInformation));
};

export const removeTeamSaga = function*(action) {
  yield call(removeTeamApi(action.payload.id));
  yield put(removeTeamSuccess());
  yield put(push('/administration'));
};

export const updateTeamSaga = function*(action) {
  yield call(updateTeamApi(action.payload));
  yield put(updateTeamSuccessful());
};

export const teamSaga = [
  takeLatest(
    requestTeam,
    withErrorHandling(getTeamSaga, () =>
      errorHandlingAction(errorReceivingTeam)
    )
  ),
  takeLatest(
    updateTeam,
    withErrorHandling(updateTeamSaga, () =>
      errorHandlingAction(updateTeamError)
    )
  ),
  takeLatest(
    removeTeam,
    withErrorHandling(removeTeamSaga, () =>
      errorHandlingAction(errorRemovingTeam)
    )
  )
];
