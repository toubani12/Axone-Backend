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
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IDomain } from 'app/entities/domain/domain.model';
import { DomainService } from 'app/entities/domain/service/domain.service';
import { UserRole } from 'app/entities/enumerations/user-role.model';
import { UserStatus } from 'app/entities/enumerations/user-status.model';
import { RecruiterService } from '../service/recruiter.service';
import { IRecruiter } from '../recruiter.model';
import { RecruiterFormService, RecruiterFormGroup } from './recruiter-form.service';

@Component({
  standalone: true,
  selector: 'jhi-recruiter-update',
  templateUrl: './recruiter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RecruiterUpdateComponent implements OnInit {
  isSaving = false;
  recruiter: IRecruiter | null = null;
  userRoleValues = Object.keys(UserRole);
  userStatusValues = Object.keys(UserStatus);

  usersSharedCollection: IUser[] = [];
  walletsCollection: IWallet[] = [];
  applicationsSharedCollection: IApplication[] = [];
  domainsSharedCollection: IDomain[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected recruiterService = inject(RecruiterService);
  protected recruiterFormService = inject(RecruiterFormService);
  protected userService = inject(UserService);
  protected walletService = inject(WalletService);
  protected applicationService = inject(ApplicationService);
  protected domainService = inject(DomainService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecruiterFormGroup = this.recruiterFormService.createRecruiterFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareWallet = (o1: IWallet | null, o2: IWallet | null): boolean => this.walletService.compareWallet(o1, o2);

  compareApplication = (o1: IApplication | null, o2: IApplication | null): boolean => this.applicationService.compareApplication(o1, o2);

  compareDomain = (o1: IDomain | null, o2: IDomain | null): boolean => this.domainService.compareDomain(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruiter }) => {
      this.recruiter = recruiter;
      if (recruiter) {
        this.updateForm(recruiter);
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
    const recruiter = this.recruiterFormService.getRecruiter(this.editForm);
    if (recruiter.id !== null) {
      this.subscribeToSaveResponse(this.recruiterService.update(recruiter));
    } else {
      this.subscribeToSaveResponse(this.recruiterService.create(recruiter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecruiter>>): void {
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

  protected updateForm(recruiter: IRecruiter): void {
    this.recruiter = recruiter;
    this.recruiterFormService.resetForm(this.editForm, recruiter);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, recruiter.relatedUser);
    this.walletsCollection = this.walletService.addWalletToCollectionIfMissing<IWallet>(this.walletsCollection, recruiter.wallet);
    this.applicationsSharedCollection = this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
      this.applicationsSharedCollection,
      ...(recruiter.applications ?? []),
    );
    this.domainsSharedCollection = this.domainService.addDomainToCollectionIfMissing<IDomain>(
      this.domainsSharedCollection,
      ...(recruiter.operationalDomains ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.recruiter?.relatedUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.walletService
      .query({ filter: 'recruiter-is-null' })
      .pipe(map((res: HttpResponse<IWallet[]>) => res.body ?? []))
      .pipe(map((wallets: IWallet[]) => this.walletService.addWalletToCollectionIfMissing<IWallet>(wallets, this.recruiter?.wallet)))
      .subscribe((wallets: IWallet[]) => (this.walletsCollection = wallets));

    this.applicationService
      .query()
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing<IApplication>(applications, ...(this.recruiter?.applications ?? [])),
        ),
      )
      .subscribe((applications: IApplication[]) => (this.applicationsSharedCollection = applications));

    this.domainService
      .query()
      .pipe(map((res: HttpResponse<IDomain[]>) => res.body ?? []))
      .pipe(
        map((domains: IDomain[]) =>
          this.domainService.addDomainToCollectionIfMissing<IDomain>(domains, ...(this.recruiter?.operationalDomains ?? [])),
        ),
      )
      .subscribe((domains: IDomain[]) => (this.domainsSharedCollection = domains));
  }
}
