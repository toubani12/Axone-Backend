import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { ITechCVSoftSkills } from '../tech-cv-soft-skills.model';
import { TechCVSoftSkillsService } from '../service/tech-cv-soft-skills.service';
import { TechCVSoftSkillsFormService, TechCVSoftSkillsFormGroup } from './tech-cv-soft-skills-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-soft-skills-update',
  templateUrl: './tech-cv-soft-skills-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechCVSoftSkillsUpdateComponent implements OnInit {
  isSaving = false;
  techCVSoftSkills: ITechCVSoftSkills | null = null;

  technicalCVSSharedCollection: ITechnicalCV[] = [];

  protected techCVSoftSkillsService = inject(TechCVSoftSkillsService);
  protected techCVSoftSkillsFormService = inject(TechCVSoftSkillsFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechCVSoftSkillsFormGroup = this.techCVSoftSkillsFormService.createTechCVSoftSkillsFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techCVSoftSkills }) => {
      this.techCVSoftSkills = techCVSoftSkills;
      if (techCVSoftSkills) {
        this.updateForm(techCVSoftSkills);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techCVSoftSkills = this.techCVSoftSkillsFormService.getTechCVSoftSkills(this.editForm);
    if (techCVSoftSkills.id !== null) {
      this.subscribeToSaveResponse(this.techCVSoftSkillsService.update(techCVSoftSkills));
    } else {
      this.subscribeToSaveResponse(this.techCVSoftSkillsService.create(techCVSoftSkills));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechCVSoftSkills>>): void {
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

  protected updateForm(techCVSoftSkills: ITechCVSoftSkills): void {
    this.techCVSoftSkills = techCVSoftSkills;
    this.techCVSoftSkillsFormService.resetForm(this.editForm, techCVSoftSkills);

    this.technicalCVSSharedCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.technicalCVSSharedCollection,
      techCVSoftSkills.technicalCV,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query()
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.techCVSoftSkills?.technicalCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.technicalCVSSharedCollection = technicalCVS));
  }
}
