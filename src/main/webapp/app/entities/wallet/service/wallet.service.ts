import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWallet, NewWallet } from '../wallet.model';

export type PartialUpdateWallet = Partial<IWallet> & Pick<IWallet, 'id'>;

export type EntityResponseType = HttpResponse<IWallet>;
export type EntityArrayResponseType = HttpResponse<IWallet[]>;

@Injectable({ providedIn: 'root' })
export class WalletService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wallets');

  create(wallet: NewWallet): Observable<EntityResponseType> {
    return this.http.post<IWallet>(this.resourceUrl, wallet, { observe: 'response' });
  }

  update(wallet: IWallet): Observable<EntityResponseType> {
    return this.http.put<IWallet>(`${this.resourceUrl}/${this.getWalletIdentifier(wallet)}`, wallet, { observe: 'response' });
  }

  partialUpdate(wallet: PartialUpdateWallet): Observable<EntityResponseType> {
    return this.http.patch<IWallet>(`${this.resourceUrl}/${this.getWalletIdentifier(wallet)}`, wallet, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWallet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWallet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWalletIdentifier(wallet: Pick<IWallet, 'id'>): number {
    return wallet.id;
  }

  compareWallet(o1: Pick<IWallet, 'id'> | null, o2: Pick<IWallet, 'id'> | null): boolean {
    return o1 && o2 ? this.getWalletIdentifier(o1) === this.getWalletIdentifier(o2) : o1 === o2;
  }

  addWalletToCollectionIfMissing<Type extends Pick<IWallet, 'id'>>(
    walletCollection: Type[],
    ...walletsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const wallets: Type[] = walletsToCheck.filter(isPresent);
    if (wallets.length > 0) {
      const walletCollectionIdentifiers = walletCollection.map(walletItem => this.getWalletIdentifier(walletItem));
      const walletsToAdd = wallets.filter(walletItem => {
        const walletIdentifier = this.getWalletIdentifier(walletItem);
        if (walletCollectionIdentifiers.includes(walletIdentifier)) {
          return false;
        }
        walletCollectionIdentifiers.push(walletIdentifier);
        return true;
      });
      return [...walletsToAdd, ...walletCollection];
    }
    return walletCollection;
  }
}
