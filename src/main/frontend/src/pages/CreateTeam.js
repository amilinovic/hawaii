import { FieldArray, Formik } from 'formik';
import React, { Component } from 'react';
import { DebounceInput } from 'react-debounce-input';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import styled from 'styled-components';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import {
  requestEmployees,
  searchEmployees
} from '../store/actions/employeesActions';
import { createTeam } from '../store/actions/teamActions';
import { getEmployees } from '../store/selectors';

const Results = styled.div`
  background: white;
  height: 150px;
  overflow: auto;
  display: ${props =>
    props.dropdownIsActive || props.inputIsActive ? 'block' : 'none'};
`;

class CreateTeam extends Component {
  state = {
    inputIsActive: false,
    dropdownIsActive: false
  };

  mouseEnter() {
    this.setState({
      dropdownIsActive: true
    });
  }

  mouseLeave() {
    this.setState({
      dropdownIsActive: false,
      inputIsActive: true
    });
    this.inputReference.focus();
  }

  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          initialValues={{
            teamApprovers: [],
            users: []
          }}
          onSubmit={this.props.createTeam}
          render={({ handleSubmit, handleChange, values }) => (
            <React.Fragment>
              <input
                className="mb-3"
                name="name"
                onChange={handleChange}
                placeholder="Team name"
              />
              <div className="position-relative">
                <DebounceInput
                  inputRef={ref => {
                    this.inputReference = ref;
                  }}
                  onFocus={() =>
                    this.setState({
                      inputIsActive: true
                    })
                  }
                  onBlur={() =>
                    this.setState({
                      inputIsActive: false
                    })
                  }
                  minLength={4}
                  debounceTimeout={300}
                  onChange={e => this.props.searchEmployees(e.target.value)}
                  placeholder="Users search"
                  className="w-100"
                />

                <Results
                  onMouseEnter={() => this.mouseEnter()}
                  onMouseLeave={() => this.mouseLeave()}
                  inputIsActive={this.state.inputIsActive}
                  dropdownIsActive={this.state.dropdownIsActive}
                  className="results position-absolute w-100 p-4 border border-top-0"
                >
                  {!this.props.employees.length ? (
                    <span>Search for employees...</span>
                  ) : (
                    this.props.employees.map(employee => {
                      return (
                        <label
                          className="d-flex justify-content-between"
                          key={employee.id}
                        >
                          {employee.fullName}
                          <div className="mb-2">
                            <FieldArray
                              name="users"
                              render={arrayHelpers => (
                                <button
                                  className="btn mr-2"
                                  type="button"
                                  onClick={() => {
                                    arrayHelpers.push(employee);
                                    this.inputReference.focus();
                                  }}
                                >
                                  Add member
                                </button>
                              )}
                            />
                            <FieldArray
                              name="teamApprovers"
                              render={arrayHelpers => (
                                <button
                                  className="btn"
                                  type="button"
                                  onClick={() => {
                                    arrayHelpers.push(employee);
                                    this.inputReference.focus();
                                  }}
                                >
                                  Add approver
                                </button>
                              )}
                            />
                          </div>
                        </label>
                      );
                    })
                  )}
                </Results>
              </div>
              <div className="d-flex justify-content-between mt-3">
                <div className="mb-5">
                  <h3>Team members</h3>
                  {!values.users.length ? (
                    <span>No members selected</span>
                  ) : (
                    values.users.map(user => {
                      return <h5 key={user.id}>{user.fullName}</h5>;
                    })
                  )}
                </div>
                <div className="mb-5">
                  <h3>Team approvers</h3>
                  {!values.teamApprovers.length ? (
                    <span>No approvers selected</span>
                  ) : (
                    values.teamApprovers.map(user => {
                      return <h5 key={user.id}>{user.fullName}</h5>;
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
  employees: getEmployees(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    { createTeam, requestEmployees, searchEmployees },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateTeam));
