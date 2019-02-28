import { FieldArray, Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Switch from 'react-switch';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import SearchDropdown from '../components/search-dropdown/SearchDropdown';
import { requestEmployees } from '../store/actions/employeesActions';
import { createTeam } from '../store/actions/teamActions';
import { getSearchEmployees } from '../store/selectors';

const validationSchema = Yup.object().shape({
  name: Yup.string().required('Required')
});

class CreateTeam extends Component {
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            name: '',
            teamApprovers: [],
            users: [],
            sendEmailToTeammatesForBonusRequestEnabled: false,
            sendEmailToTeammatesForSicknessRequestEnabled: false,
            sendEmailToTeammatesForAnnualRequestEnabled: false
          }}
          onSubmit={this.props.createTeam}
          render={({
            handleSubmit,
            handleChange,
            values,
            errors,
            touched,
            setFieldValue
          }) => (
            <React.Fragment>
              <div className="mb-3">
                <input
                  className={`${
                    errors.name && touched.name
                      ? 'border border-danger'
                      : 'border'
                  } w-100`}
                  name="name"
                  onChange={handleChange}
                  placeholder="Team name"
                />
              </div>
              <div className="d-flex justify-content-between mb-3">
                <h5>Send email to teammates for annual request</h5>
                <Switch
                  onChange={() =>
                    setFieldValue(
                      'sendEmailToTeammatesForAnnualRequestEnabled',
                      !values.sendEmailToTeammatesForAnnualRequestEnabled
                    )
                  }
                  checked={values.sendEmailToTeammatesForAnnualRequestEnabled}
                  name="sendEmailToTeammatesForAnnualRequestEnabled"
                />
              </div>
              <div className="d-flex justify-content-between mb-3">
                <h5>Send email to teammates for bonus request</h5>
                <Switch
                  onChange={() =>
                    setFieldValue(
                      'sendEmailToTeammatesForBonusRequestEnabled',
                      !values.sendEmailToTeammatesForBonusRequestEnabled
                    )
                  }
                  checked={values.sendEmailToTeammatesForBonusRequestEnabled}
                  name="sendEmailToTeammatesForBonusRequestEnabled"
                />
              </div>
              <div className="d-flex justify-content-between mb-3">
                <h5>Send email to teammates for bonus request</h5>
                <Switch
                  onChange={() =>
                    setFieldValue(
                      'sendEmailToTeammatesForSicknessRequestEnabled',
                      !values.sendEmailToTeammatesForSicknessRequestEnabled
                    )
                  }
                  checked={values.sendEmailToTeammatesForSicknessRequestEnabled}
                  name="sendEmailToTeammatesForSicknessRequestEnabled"
                />
              </div>
              <SearchDropdown results={this.props.employees} />
              <div className="d-flex justify-content-between mt-3">
                <div className="mb-5">
                  <h3>Team members</h3>
                  {!values.users.length ? (
                    <span>No members selected</span>
                  ) : (
                    values.users.map(user => {
                      return (
                        <h5 key={user.id}>
                          {user.fullName}
                          <FieldArray
                            name="users"
                            render={arrayHelpers => (
                              <span
                                className="text-danger ml-2"
                                onClick={() => {
                                  arrayHelpers.pop(user);
                                }}
                              >
                                x
                              </span>
                            )}
                          />
                        </h5>
                      );
                    })
                  )}
                </div>
                <div className="mb-5">
                  <h3>Team approvers</h3>
                  {!values.teamApprovers.length ? (
                    <span>No approvers selected</span>
                  ) : (
                    values.teamApprovers.map(user => {
                      return (
                        <h5 key={user.id}>
                          {user.fullName}
                          <FieldArray
                            name="teamApprovers"
                            render={arrayHelpers => (
                              <span
                                className="text-danger ml-2"
                                onClick={() => {
                                  arrayHelpers.pop(user);
                                }}
                              >
                                x
                              </span>
                            )}
                          />
                        </h5>
                      );
                    })
                  )}
                </div>
              </div>
              <button className="btn" onClick={handleSubmit} type="submit">
                Create team
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  employees: getSearchEmployees(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createTeam, requestEmployees }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateTeam));
