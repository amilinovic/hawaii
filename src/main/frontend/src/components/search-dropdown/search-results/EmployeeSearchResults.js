import { FieldArray } from 'formik';
import React, { Component } from 'react';
import Loading from '../../loading/Loading';

export default class EmployeeSearchResults extends Component {
  render() {
    if (this.props.employees.isFetching) {
      return <Loading />;
    }

    if (this.props.employees.results.length === 0) {
      return <span>No results</span>;
    }

    return this.props.employees.results.map(result => {
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
    });
  }
}
