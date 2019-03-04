import { FieldArray } from 'formik';
import React, { Component } from 'react';

export default class EmployeeSearchResults extends Component {
  render() {
    return this.props.employees.isFetching ? (
      <span>Loading...</span>
    ) : !this.props.employees.results ? null : !this.props.employees
        .isFetching && !this.props.employees.results.length ? (
      <span>No results</span>
    ) : (
      this.props.employees.results.map(result => {
        return (
          <label className="d-flex justify-content-between" key={result.id}>
            {result.fullName}
            <div className="mb-2">
              <FieldArray
                name="users"
                render={arrayHelpers => (
                  <button
                    className="btn mr-2"
                    type="button"
                    onClick={() => {
                      arrayHelpers.push(result);
                      this.props.inputReference.focus();
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
                      arrayHelpers.push(result);
                      this.props.inputReference.focus();
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
    );
  }
}
