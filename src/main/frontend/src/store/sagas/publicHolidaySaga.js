import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingPublicHoliday,
  requestPublicHoliday
} from '../actions/publicHolidayActions';
import {
  createTeam,
  createTeamError,
  createTeamSuccessful,
  errorRemovingTeam,
  removeTeam,
  removeTeamSuccess,
  updateTeam,
  updateTeamError,
  updateTeamSuccessful
} from '../actions/teamActions';
import {
  createTeamApi,
  removeTeamApi,
  updateTeamApi
} from '../services/teamService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';
import { toastrSuccess } from './toastrHelperSaga';

const getPublicHolidaySaga = function*(action) {
  const publicHolidayInformation = yield call(
    getPublicHolidayApi(action.payload)
  );
  yield put(receivePublicHoliday(publicHolidayInformation));
};

const removeTeamSaga = function*(action) {
  yield call(removeTeamApi(action.payload.id));
  yield put(removeTeamSuccess());
  yield put(toastrSuccess('Succesfully removed team'));
  yield put(push('/administration'));
};

const updateTeamSaga = function*(action) {
  yield call(updateTeamApi(action.payload));
  yield put(updateTeamSuccessful());
  yield put(toastrSuccess('Succesfully updated team'));
};

const createTeamSaga = function*(action) {
  yield call(createTeamApi(action.payload));
  yield put(createTeamSuccessful());
  yield put(push('/administration'));
  yield put(toastrSuccess('Successfully created team'));
};

export const publicHolidaySaga = [
  takeLatest(
    requestPublicHoliday,
    withErrorHandling(
      getPublicHolidaySaga,
      genericErrorHandler(errorReceivingPublicHoliday)
    )
  ),
  takeLatest(
    updateTeam,
    withErrorHandling(updateTeamSaga, genericErrorHandler(updateTeamError))
  ),
  takeLatest(
    removeTeam,
    withErrorHandling(removeTeamSaga, genericErrorHandler(errorRemovingTeam))
  ),
  takeLatest(
    createTeam,
    withErrorHandling(createTeamSaga, genericErrorHandler(createTeamError))
  )
];
