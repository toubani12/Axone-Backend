import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IAppAccountType } from 'app/entities/app-account-type/app-account-type.model';
import { AppAccountTypeService } from 'app/entities/app-account-type/service/app-account-type.service';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { AppAccountService } from '../service/app-account.service';
import { IAppAccount } from '../app-account.model';
import { AppAccountFormService, AppAccountFormGroup } from './app-account-form.service';

@Component({
  standalone: true,
  selector: 'jhi-app-account-update',
  templateUrl: './app-account-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppAccountUpdateComponent implements OnInit {
  isSaving = false;
  appAccount: IAppAccount | null = null;

  usersSharedCollection: IUser[] = [];
  appAccountTypesSharedCollection: IAppAccountType[] = [];
  providersSharedCollection: IProvider[] = [];
  employersSharedCollection: IEmployer[] = [];

  protected appAccountService = inject(AppAccountService);
  protected appAccountFormService = inject(AppAccountFormService);
  protected userService = inject(UserService);
  protected appAccountTypeService = inject(AppAccountTypeService);
  protected providerService = inject(ProviderService);
  protected employerService = inject(EmployerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppAccountFormGroup = this.appAccountFormService.createAppAccountFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareAppAccountType = (o1: IAppAccountType | null, o2: IAppAccountType | null): boolean =>
    this.appAccountTypeService.compareAppAccountType(o1, o2);

  compareProvider = (o1: IProvider | null, o2: IProvider | null): boolean => this.providerService.compareProvider(o1, o2);

  compareEmployer = (o1: IEmployer | null, o2: IEmployer | null): boolean => this.employerService.compareEmployer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appAccount }) => {
      this.appAccount = appAccount;
      if (appAccount) {
        this.updateForm(appAccount);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appAccount = this.appAccountFormService.getAppAccount(this.editForm);
    if (appAccount.id !== null) {
      this.subscribeToSaveResponse(this.appAccountService.update(appAccount));
    } else {
      this.subscribeToSaveResponse(this.appAccountService.create(appAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppAccount>>): void {
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

  protected updateForm(appAccount: IAppAccount): void {
    this.appAccount = appAccount;
    this.appAccountFormService.resetForm(this.editForm, appAccount);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, appAccount.relatedUser);
    this.appAccountTypesSharedCollection = this.appAccountTypeService.addAppAccountTypeToCollectionIfMissing<IAppAccountType>(
      this.appAccountTypesSharedCollection,
      ...(appAccount.types ?? []),
    );
    this.providersSharedCollection = this.providerService.addProviderToCollectionIfMissing<IProvider>(
      this.providersSharedCollection,
      ...(appAccount.providers ?? []),
    );
    this.employersSharedCollection = this.employerService.addEmployerToCollectionIfMissing<IEmployer>(
      this.employersSharedCollection,
      appAccount.ifEmployer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.appAccount?.relatedUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.appAccountTypeService
      .query()
      .pipe(map((res: HttpResponse<IAppAccountType[]>) => res.body ?? []))
      .pipe(
        map((appAccountTypes: IAppAccountType[]) =>
          this.appAccountTypeService.addAppAccountTypeToCollectionIfMissing<IAppAccountType>(
            appAccountTypes,
            ...(this.appAccount?.types ?? []),
          ),
        ),
      )
      .subscribe((appAccountTypes: IAppAccountType[]) => (this.appAccountTypesSharedCollection = appAccountTypes));

    this.providerService
      .query()
      .pipe(map((res: HttpResponse<IProvider[]>) => res.body ?? []))
      .pipe(
        map((providers: IProvider[]) =>
          this.providerService.addProviderToCollectionIfMissing<IProvider>(providers, ...(this.appAccount?.providers ?? [])),
        ),
      )
      .subscribe((providers: IProvider[]) => (this.providersSharedCollection = providers));

    this.employerService
      .query()
      .pipe(map((res: HttpResponse<IEmployer[]>) => res.body ?? []))
      .pipe(
        map((employers: IEmployer[]) =>
          this.employerService.addEmployerToCollectionIfMissing<IEmployer>(employers, this.appAccount?.ifEmployer),
        ),
      )
      .subscribe((employers: IEmployer[]) => (this.employersSharedCollection = employers));
  }
}
