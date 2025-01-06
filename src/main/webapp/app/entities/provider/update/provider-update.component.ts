import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppAccount } from 'app/entities/app-account/app-account.model';
import { AppAccountService } from 'app/entities/app-account/service/app-account.service';
import { IProvider } from '../provider.model';
import { ProviderService } from '../service/provider.service';
import { ProviderFormService, ProviderFormGroup } from './provider-form.service';

@Component({
  standalone: true,
  selector: 'jhi-provider-update',
  templateUrl: './provider-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProviderUpdateComponent implements OnInit {
  isSaving = false;
  provider: IProvider | null = null;

  appAccountsSharedCollection: IAppAccount[] = [];

  protected providerService = inject(ProviderService);
  protected providerFormService = inject(ProviderFormService);
  protected appAccountService = inject(AppAccountService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProviderFormGroup = this.providerFormService.createProviderFormGroup();

  compareAppAccount = (o1: IAppAccount | null, o2: IAppAccount | null): boolean => this.appAccountService.compareAppAccount(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ provider }) => {
      this.provider = provider;
      if (provider) {
        this.updateForm(provider);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const provider = this.providerFormService.getProvider(this.editForm);
    if (provider.id !== null) {
      this.subscribeToSaveResponse(this.providerService.update(provider));
    } else {
      this.subscribeToSaveResponse(this.providerService.create(provider));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvider>>): void {
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

  protected updateForm(provider: IProvider): void {
    this.provider = provider;
    this.providerFormService.resetForm(this.editForm, provider);

    this.appAccountsSharedCollection = this.appAccountService.addAppAccountToCollectionIfMissing<IAppAccount>(
      this.appAccountsSharedCollection,
      ...(provider.appAccounts ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appAccountService
      .query()
      .pipe(map((res: HttpResponse<IAppAccount[]>) => res.body ?? []))
      .pipe(
        map((appAccounts: IAppAccount[]) =>
          this.appAccountService.addAppAccountToCollectionIfMissing<IAppAccount>(appAccounts, ...(this.provider?.appAccounts ?? [])),
        ),
      )
      .subscribe((appAccounts: IAppAccount[]) => (this.appAccountsSharedCollection = appAccounts));
  }
}
