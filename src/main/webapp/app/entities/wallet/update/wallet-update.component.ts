import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppAccount } from 'app/entities/app-account/app-account.model';
import { AppAccountService } from 'app/entities/app-account/service/app-account.service';
import { WalletStatus } from 'app/entities/enumerations/wallet-status.model';
import { WalletService } from '../service/wallet.service';
import { IWallet } from '../wallet.model';
import { WalletFormService, WalletFormGroup } from './wallet-form.service';

@Component({
  standalone: true,
  selector: 'jhi-wallet-update',
  templateUrl: './wallet-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WalletUpdateComponent implements OnInit {
  isSaving = false;
  wallet: IWallet | null = null;
  walletStatusValues = Object.keys(WalletStatus);

  relatedToAccountsCollection: IAppAccount[] = [];

  protected walletService = inject(WalletService);
  protected walletFormService = inject(WalletFormService);
  protected appAccountService = inject(AppAccountService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WalletFormGroup = this.walletFormService.createWalletFormGroup();

  compareAppAccount = (o1: IAppAccount | null, o2: IAppAccount | null): boolean => this.appAccountService.compareAppAccount(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wallet }) => {
      this.wallet = wallet;
      if (wallet) {
        this.updateForm(wallet);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wallet = this.walletFormService.getWallet(this.editForm);
    if (wallet.id !== null) {
      this.subscribeToSaveResponse(this.walletService.update(wallet));
    } else {
      this.subscribeToSaveResponse(this.walletService.create(wallet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWallet>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(wallet: IWallet): void {
    this.wallet = wallet;
    this.walletFormService.resetForm(this.editForm, wallet);

    this.relatedToAccountsCollection = this.appAccountService.addAppAccountToCollectionIfMissing<IAppAccount>(
      this.relatedToAccountsCollection,
      wallet.relatedToAccount,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appAccountService
      .query({ filter: 'relatedwallet-is-null' })
      .pipe(map((res: HttpResponse<IAppAccount[]>) => res.body ?? []))
      .pipe(
        map((appAccounts: IAppAccount[]) =>
          this.appAccountService.addAppAccountToCollectionIfMissing<IAppAccount>(appAccounts, this.wallet?.relatedToAccount),
        ),
      )
      .subscribe((appAccounts: IAppAccount[]) => (this.relatedToAccountsCollection = appAccounts));
  }
}
