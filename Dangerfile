# Sometimes it's a README fix, or something like that - which isn't relevant for
# including in a project's CHANGELOG for example
require "open3"
require "humanize-bytes"

declared_trivial = github.pr_title.include? "#trivial"

def apk_size()
  File.size("app/build/outputs/apk/debug/app-debug.apk")
end
message("Apk Size #{apk_size}")

def get_size()
  curr_size = apk_size()
  master_size = 3553160
  difference = Humanize::Byte.new(curr_size - master_size)
  message(difference)
  message "APK size has changed! Now: %.2fMb; on develop: %.2fMb. Difference: %0.2fKb" % [Humanize::Byte.new(curr_size).to_m.value, Humanize::Byte.new(master_size).to_m.value, difference.to_k.value] if difference.to_k.value.abs > 512 else message "APK Size same!"
end

get_size()

def check_methods_ref_count()
  methods_ref_count_current = "403"
  methods_ref_count_master = "200"
  difference = methods_ref_count_current.to_i - methods_ref_count_master.to_i
  if difference >= 200
    warn "method count has changed on master #{methods_ref_count_master} ; current #{methods_ref_count_current}"
  else
    message "method count under threshold"
  end
end

check_methods_ref_count()
# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

# Don't let testing shortcuts get into master by accident
fail("fdescribe left in tests") if `grep -r fdescribe specs/ `.length > 1
fail("fit left in tests") if `grep -r fit specs/ `.length > 1
