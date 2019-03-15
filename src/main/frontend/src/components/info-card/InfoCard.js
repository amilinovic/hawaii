import React, { Component } from 'react';
import styled from 'styled-components';
import Loading from '../loading/Loading';

const Card = styled.div`
  background: white;
`;

const title = ['Taken', 'Pending', 'Remaining'];

export default class InfoCard extends Component {
  filterAllowance = (filter, allowance) => {
    const filteredAllowance = [];

    Object.keys(allowance).map(function(key, index) {
      if (key.toLocaleLowerCase().includes(filter)) {
        filteredAllowance.push({
          id: index,
          hours: allowance[key]
        });
      }
      return null;
    });

    return filteredAllowance;
  };

  render() {
    return (
      <Card className="p-3 border rounded">
        <h1 className="border-bottom mb-3 pb-1">{this.props.title}</h1>
        {!this.props.allowance ? (
          <Loading />
        ) : (
          this.filterAllowance(
            this.props.allowanceType,
            this.props.allowance
          ).map((item, index) => {
            return (
              <div
                key={item.id}
                className="d-flex justify-content-between mb-2"
              >
                <span>{title[index]}</span>
                <span>{item.hours / 8} days</span>
              </div>
            );
          })
        )}
      </Card>
    );
  }
}
