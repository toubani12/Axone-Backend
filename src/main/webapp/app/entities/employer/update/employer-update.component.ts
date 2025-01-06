import { Component, inject, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { UserRole } from 'app/entities/enumerations/user-role.model';
import { UserStatus } from 'app/entities/enumerations/user-status.model';
import { EmployerService } from '../service/employer.service';
import { IEmployer } from '../employer.model';
import { EmployerFormService, EmployerFormGroup } from './employer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-employer-update',
  templateUrl: './employer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmployerUpdateComponent implements OnInit {
  isSaving = false;
  employer: IEmployer | null = null;
  userRoleValues = Object.keys(UserRole);
  userStatusValues = Object.keys(UserStatus);

  usersSharedCollection: IUser[] = [];
  walletsCollection: IWallet[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected employerService = inject(EmployerService);
  protected employerFormService = inject(EmployerFormService);
  protected userService = inject(UserService);
  protected walletService = inject(WalletService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmployerFormGroup = this.employerFormService.createEmployerFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareWallet = (o1: IWallet | null, o2: IWallet | null): boolean => this.walletService.compareWallet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employer }) => {
      this.employer = employer;
      if (employer) {
        this.updateForm(employer);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('hrSolutionApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employer = this.employerFormService.getEmployer(this.editForm);
    if (employer.id !== null) {
      this.subscribeToSaveResponse(this.employerService.update(employer));
    } else {
      this.subscribeToSaveResponse(this.employerService.create(employer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployer>>): void {
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

  protected updateForm(employer: IEmployer): void {
    this.employer = employer;
    this.employerFormService.resetForm(this.editForm, employer);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, employer.relatedUser);
    this.walletsCollection = this.walletService.addWalletToCollectionIfMissing<IWallet>(this.walletsCollection, employer.wallet);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.employer?.relatedUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.walletService
      .query({ filter: 'employer-is-null' })
      .pipe(map((res: HttpResponse<IWallet[]>) => res.body ?? []))
      .pipe(map((wallets: IWallet[]) => this.walletService.addWalletToCollectionIfMissing<IWallet>(wallets, this.employer?.wallet)))
      .subscribe((wallets: IWallet[]) => (this.walletsCollection = wallets));
  }
}
