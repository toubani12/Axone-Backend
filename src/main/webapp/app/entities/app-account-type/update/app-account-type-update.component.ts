import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppAccount } from 'app/entities/app-account/app-account.model';
import { AppAccountService } from 'app/entities/app-account/service/app-account.service';
import { IAppAccountType } from '../app-account-type.model';
import { AppAccountTypeService } from '../service/app-account-type.service';
import { AppAccountTypeFormService, AppAccountTypeFormGroup } from './app-account-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-app-account-type-update',
  templateUrl: './app-account-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppAccountTypeUpdateComponent implements OnInit {
  isSaving = false;
  appAccountType: IAppAccountType | null = null;

  appAccountsSharedCollection: IAppAccount[] = [];

  protected appAccountTypeService = inject(AppAccountTypeService);
  protected appAccountTypeFormService = inject(AppAccountTypeFormService);
  protected appAccountService = inject(AppAccountService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppAccountTypeFormGroup = this.appAccountTypeFormService.createAppAccountTypeFormGroup();

  compareAppAccount = (o1: IAppAccount | null, o2: IAppAccount | null): boolean => this.appAccountService.compareAppAccount(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appAccountType }) => {
      this.appAccountType = appAccountType;
      if (appAccountType) {
        this.updateForm(appAccountType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appAccountType = this.appAccountTypeFormService.getAppAccountType(this.editForm);
    if (appAccountType.id !== null) {
      this.subscribeToSaveResponse(this.appAccountTypeService.update(appAccountType));
    } else {
      this.subscribeToSaveResponse(this.appAccountTypeService.create(appAccountType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppAccountType>>): void {
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

  protected updateForm(appAccountType: IAppAccountType): void {
    this.appAccountType = appAccountType;
    this.appAccountTypeFormService.resetForm(this.editForm, appAccountType);

    this.appAccountsSharedCollection = this.appAccountService.addAppAccountToCollectionIfMissing<IAppAccount>(
      this.appAccountsSharedCollection,
      ...(appAccountType.appAccounts ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appAccountService
      .query()
      .pipe(map((res: HttpResponse<IAppAccount[]>) => res.body ?? []))
      .pipe(
        map((appAccounts: IAppAccount[]) =>
          this.appAccountService.addAppAccountToCollectionIfMissing<IAppAccount>(appAccounts, ...(this.appAccountType?.appAccounts ?? [])),
        ),
      )
      .subscribe((appAccounts: IAppAccount[]) => (this.appAccountsSharedCollection = appAccounts));
  }
}
