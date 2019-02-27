import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createTeam,
  createTeamError,
  createTeamSuccessful,
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
  createTeamApi,
  getTeamApi,
  removeTeamApi,
  updateTeamApi
} from '../services/teamService';
import { toastrError, toastrSuccess } from './toastrHelperSaga';

export const getTeamSaga = function*(action) {
  try {
    const teamInformation = yield call(getTeamApi(action.payload));
    yield put(receiveTeam(teamInformation));
  } catch (error) {
    yield put(errorReceivingTeam(error));
    yield put(toastrError(error.message));
  }
};

export const removeTeamSaga = function*(action) {
  try {
    yield call(removeTeamApi(action.payload.id));
    yield put(removeTeamSuccess());
    yield put(toastrSuccess('Succesfully removed team'));
    yield put(push('/administration'));
  } catch (error) {
    yield put(errorRemovingTeam(error));
    yield put(toastrError(error.message));
  }
};

export const updateTeamSaga = function*(action) {
  try {
    yield call(updateTeamApi(action.payload));
    yield put(updateTeamSuccessful());
    yield put(toastrSuccess('Succesfully updated team'));
  } catch (error) {
    yield put(updateTeamError(error));
    yield put(toastrError(error.message));
  }
};

export const createTeamSaga = function*(action) {
  try {
    yield call(createTeamApi(action.payload));
    yield put(createTeamSuccessful());
    yield put(push('/administration'));
    yield put(toastrSuccess('Successfully created team'));
  } catch (error) {
    yield put(createTeamError(error));
    yield put(toastrError(error.message));
  }
};

export const teamSaga = [
  takeLatest(requestTeam, getTeamSaga),
  takeLatest(updateTeam, updateTeamSaga),
  takeLatest(createTeam, createTeamSaga),
  takeLatest(removeTeam, removeTeamSaga)
];
