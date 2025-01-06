import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContract, NewContract } from '../contract.model';

export type PartialUpdateContract = Partial<IContract> & Pick<IContract, 'id'>;

export type EntityResponseType = HttpResponse<IContract>;
export type EntityArrayResponseType = HttpResponse<IContract[]>;

@Injectable({ providedIn: 'root' })
export class ContractService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contracts');

  create(contract: NewContract): Observable<EntityResponseType> {
    return this.http.post<IContract>(this.resourceUrl, contract, { observe: 'response' });
  }

  update(contract: IContract): Observable<EntityResponseType> {
    return this.http.put<IContract>(`${this.resourceUrl}/${this.getContractIdentifier(contract)}`, contract, { observe: 'response' });
  }

  partialUpdate(contract: PartialUpdateContract): Observable<EntityResponseType> {
    return this.http.patch<IContract>(`${this.resourceUrl}/${this.getContractIdentifier(contract)}`, contract, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContract>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContract[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContractIdentifier(contract: Pick<IContract, 'id'>): number {
    return contract.id;
  }

  compareContract(o1: Pick<IContract, 'id'> | null, o2: Pick<IContract, 'id'> | null): boolean {
    return o1 && o2 ? this.getContractIdentifier(o1) === this.getContractIdentifier(o2) : o1 === o2;
  }

  addContractToCollectionIfMissing<Type extends Pick<IContract, 'id'>>(
    contractCollection: Type[],
    ...contractsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contracts: Type[] = contractsToCheck.filter(isPresent);
    if (contracts.length > 0) {
      const contractCollectionIdentifiers = contractCollection.map(contractItem => this.getContractIdentifier(contractItem));
      const contractsToAdd = contracts.filter(contractItem => {
        const contractIdentifier = this.getContractIdentifier(contractItem);
        if (contractCollectionIdentifiers.includes(contractIdentifier)) {
          return false;
        }
        contractCollectionIdentifiers.push(contractIdentifier);
        return true;
      });
      return [...contractsToAdd, ...contractCollection];
    }
    return contractCollection;
  }
}
